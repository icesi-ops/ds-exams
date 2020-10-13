base:
  'roles:web':
    - match: grain
    - IZI-web
    - IZI-nodejs

  'roles:db':
    - match: grain
    - IZI-db

  'roles:lb':
    - match: grain
    - IZI-lb
