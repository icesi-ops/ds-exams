install_front_npm_dependencies:
    npm.bootstrap:
      - name: /srv/Scripts/ds-exams/app/http/app

run_front:
    cmd.run:
      - name: "cd /srv/Scripts/ds-exams/app/http/app && npm start"

run_back:
    cmd.run:
      - name: "cd /srv/Scripts/ds-exams/app && FLASK_APP=$PWD/app/http/api/endpoints.py FLASK_ENV=development pipenv run python -m flask run --port 4433"
