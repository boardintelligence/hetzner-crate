(ns hetzner-crate.crate
  "Crate with functions for Hetzner servers"
  (:require [pallet.actions :as actions]
            [pallet.crate :as crate]
            [pallet.crate.automated-admin-user :as admin-user]
            [pallet.crate.etc-hosts :as etc-hosts]
            [pallet.crate.ssh-key :as ssh-key]
            [pallet.environment :as env]
            [pallet.crate :refer [defplan]]))

(defplan set-timezone
  "Set timezone of host"
  [timezone]
  (actions/remote-file "/etc/timezone" :content timezone)
  (actions/exec-checked-script
   "update time zone"
   ("dpkg-reconfigure --frontend noninteractive tzdata")))

(defplan initial-setup
  []
  (let [node-hostname (crate/target-name)
        host-config (env/get-environment [:host-config node-hostname])
        admin-username (get-in host-config [:admin-user :username])
        admin-ssh-public-key-path (get-in host-config [:admin-user :ssh-public-key-path])
        timezone (:timezone host-config)
        rootpass (:rootpass host-config)
        ip (:ip host-config)
        private-ip (:private-ip host-config)
        private-hostname (:private-hostname host-config)]

    (set-timezone timezone)

    ;; /etc/hosts and hostname
    (etc-hosts/add-host ip [node-hostname])
    (if-not (nil? private-ip)
      (etc-hosts/add-host private-ip [private-hostname]))
    (etc-hosts/hosts)
    (etc-hosts/set-hostname)

    ;; rootpass
    (actions/exec-checked-script
     "change root passwd"
     (pipe ("echo" ~(format "root:%s" rootpass))
           ("chpasswd")))

    ;; admin user
    (if (= admin-username "root")
      (ssh-key/authorize-key "root" (slurp admin-ssh-public-key-path))
      (admin-user/automated-admin-user admin-username admin-ssh-public-key-path))

    ;; update package manager
    (actions/package-manager :update)))
