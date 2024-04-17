from flask import Flask

# Crea una instancia de la aplicación Flask
app = Flask(__name__)

# Define una ruta '/' que devuelve el string solicitado
@app.route('/')
def hello():
    return 'Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch'

# Si se ejecuta este script directamente, inicia el servidor
if __name__ == '__main__':
    app.run(host='0.0.0.0')
