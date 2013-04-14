(ns hetzner-crate.specs
  "Pallet specs for Hetzner located servers"
  (:require [pallet.api :as api]
            [hetzner-crate.crate :as hetzner]))

(def
  ^{:doc "Physical Hetzner server. Just basic config to be able to use pallet as normal with it."}
  hetzner-server
  (api/server-spec
   :phases
   {:hetzner-bootstrap (api/plan-fn (hetzner/initial-setup))}))

(def
  ^{:doc "LXC container on a Hetzner server. Just basic config to be able to use pallet as normal with it."}
  hetzner-container
  (api/server-spec
   :phases
   {:configure (api/plan-fn (hetzner/initial-setup)
                            (hetzner/set-hetzner-apt-mirror))}))
