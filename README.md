# hetzner-crate

A Pallet crate to work with dedicated servers at Hetzner data centre.

A few utility functions to initialize a freshly provisioned dedicated
Ubuntu server at Hetzner assuming all you have is the machine's IP and
first root password appointed by Hetzner.

It let's you:
* set the machine's timezone
* set the root password to something you have chosen
* set the machine's hostname and update /etc/hosts
* prepare an admin user for use with pallet

## Usage

*hetzner-crate* uses [pallet-nodelist-helpers](https://github.com/boardintelligence/pallet-nodelist-helpers)
and the format of hosts-config, apart from the standard ones, are:

    {"host.to.configure" {:first-rootpass "appointed password"
                          :rootpass "chosen password"
                          :timezone "Etc/UTC"}}

The *hetzner-crate/api* namespace provides a utility function *hetzner-initial-setup*:

    (hetzner-initial-setup "hostname.to.set.up")

There's a server spec included called *hetzner-server* in the *hetzner-crate/specs*
namespace with a phase *hetzner-bootstrap* that you can use and that *hetzner-initial-setup*
relies on.

## License

Copyright © 2013 Board Intelligence

Distributed under the MIT License, see
[http://boardintelligence.mit-license.org](http://boardintelligence.mit-license.or)
for details.
