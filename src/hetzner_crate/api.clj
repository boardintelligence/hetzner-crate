(ns hetzner-crate.api
  "API for working with Hetzner servers"
  (:require
   [pallet.algo.fsmop :as fsmop]
   [pallet.api :as api]
   [pallet-nodelist-helpers :as helpers]))

(defn- get-root-user
  [hostname & {:keys [use-firstpass]}]
  (let [pass-key (if use-firstpass :first-rootpass :rootpass)
        rootpass (get-in helpers/*nodelist-hosts-config* [hostname pass-key])]
    (api/make-user "root"
                   :password rootpass
                   :private-key-path nil
                   :public-key-path nil
                   :no-sudo true)))

(defn hetzner-initial-setup
  "Perform the minimal initial setup needed and reboot machine.
  Assumes that the entry in the host config for this hostname includes
  the keys :first-rootpass, :rootpass and :timezone"
  [hostname]
  (helpers/ensure-nodelist-bindings)
  (let [result (helpers/lift-one-node-and-phase hostname
                                                (get-root-user hostname :use-firstpass true)
                                                :hetzner-bootstrap
                                                {})]
    (when (fsmop/failed? result)
      (throw (IllegalStateException. "Failed initial setup of Hetzner server!")))
    result))
