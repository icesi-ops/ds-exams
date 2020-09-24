include:
    - nodejs

install_npm_dependencies:
    npm.bootstrap:
      - name: /srv/ds-exams/aik-app-api

run_front:
    cmd.run:
      - name: "node /srv/ds-exams/Front-end/server.js"

run_back:
    cmd.run:
      - name: "node /srv/ds-exams/Back-end/server.js"
