include:
    - nodejs
    
python-pip:
  pkg.installed

install_back_dependencies:
    pip.installed:
      - bin_env: '/usr/bin/pip'
      - require:
        - pkg: python-pip
      - pkgs:
        - flask_cors
        - marshmallow
        - pymongo
        - pyjwt
        - flask
        - pipenv

install_front_npm_dependencies:
    npm.bootstrap:
      - name: /srv/ds-exams/app/http/app

run_front:
    cmd.run:
      - name: "cd /srv/ds-exams/app/http/app && npm start"

run_back:
    cmd.run:
      - name: "cd /srv/ds-exams/app && FLASK_APP=$PWD/app/http/api/endpoints.py FLASK_ENV=development pipenv run python -m flask run --port 4433"
