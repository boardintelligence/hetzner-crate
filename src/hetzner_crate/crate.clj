(ns hetzner-crate.crate
  "Crate with functions for Hetzner servers"
  (:require
   [pallet.actions :as actions]
   [pallet.environment :as env]
   [pallet.crate :as crate]
   [pallet.crate.etc-hosts :as etc-hosts]
   [pallet.crate.ssh-key :as ssh-key]
   [pallet.crate.automated-admin-user :as admin-user])
  (:use [pallet.crate :only [def-plan-fn]]
        [clojure.algo.monads :only [m-when m-when-not m-result]]))

(def-plan-fn set-timezone
  "Set timezone of host"
  [timezone]
  []
  (actions/remote-file "/etc/timezone" :content timezone)
  (actions/exec-checked-script
   "update time zone"
   (dpkg-reconfigure --frontend noninteractive tzdata)))

(def-plan-fn initial-setup
  []
  [node-hostname crate/target-name
   host-config (env/get-environment [:host-config node-hostname])
   admin-username (m-result (get-in host-config [:admin-user :username]))
   admin-ssh-public-key-path (m-result (get-in host-config [:admin-user :ssh-public-key-path]))
   timezone (m-result (:timezone host-config))
   rootpass (m-result (:rootpass host-config))
   ip (m-result (:ip host-config))]

  (set-timezone timezone)

  ;; /etc/hosts and hostname
  (etc-hosts/host ip node-hostname)
  etc-hosts/hosts
  (etc-hosts/set-hostname)

  ;; rootpass
  (actions/exec-checked-script
   "change root passwd"
   (pipe (echo ~(format "root:%s" rootpass))
         (chpasswd)))

  ;; admin user
  (if (= admin-username "root")
    (ssh-key/authorize-key "root" (slurp admin-ssh-public-key-path))
    (admin-user/automated-admin-user admin-username admin-ssh-public-key-path))

  ;; reboot system for changes to take effect
  (actions/exec-checked-script
   "reboot system"
   (pipe (echo "reboot")
         (at -M "now + 1 minute"))))
