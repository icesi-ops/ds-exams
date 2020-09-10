base:

  'roles:web':
    - match: grain
    - aik-ui
    - nodejs

  'roles:db':
    - match: grain
    - aik-api
    - nodejs

  'roles:lb':
    - match: grain
    - aik-api
    - nodejs
