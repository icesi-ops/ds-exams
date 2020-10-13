install_haproxy:
  pkg.installed:
    - pkgs:
      - haproxy

/etc/haproxy/haproxy.cfg:
  file.append:
    - text: |
        
        frontend main
            bind *:8080
            default_backend nodes
        
        backend nodes
            balance roundrobin
            server web-1 192.168.33.11:4433 check
            server web-2 192.168.33.12:4433 check

run_haproxy:
  cmd.run:
<<<<<<< HEAD
    - name: sudo systemctl restart haproxy

=======
    - name: sudo systemctl start haproxy
>>>>>>> 9a06e706b900018a12003ad3adaac440442ae1b8
