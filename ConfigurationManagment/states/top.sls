base:

  'roles:web':
    - match: grain
    - IZI-web
    - IZI-nodejs

  'roles:db':
    - match: grain
    - IZI-db
    - IZI-nodejs

  'roles:lb':
    - match: grain
    - IZI-lb
    - IZI-nodejs
