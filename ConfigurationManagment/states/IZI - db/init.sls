include:
    - nodejs

install_npm_dependencies:
    npm.bootstrap:
      - name: /srv/aik-portal/aik-app-api

run_front:
    cmd.run:
      - name: "node /srv/aik-portal/Front-end/server.js"

run_back:
    cmd.run:
      - name: "node /srv/aik-portal/Back-end/server.js"
