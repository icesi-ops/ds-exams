from smb.SMBConnection import SMBConnection
from fastapi import FastAPI, UploadFile , Response
from fastapi.middleware.cors import CORSMiddleware
import consul

app = FastAPI()

# Configura el middleware CORS con orígenes específicos
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],  # Reemplaza con el origen de tu aplicación de frontend
    allow_credentials=True,
    allow_methods=["GET", "POST", "OPTIONS"],  # Ajusta según las necesidades de tu aplicación
    allow_headers=["*"],  # Puedes ajustar esto según las necesidades
)

userID = 'user'
password = 'password'
client_machine_name = '192.168.20.70'
server_name = 'servername'
server_ip = 'samba'
domain_name = 'domainname'
share_name = 'public'
conn = SMBConnection(userID, password, client_machine_name, server_name, domain=domain_name, use_ntlm_v2=True,
                     is_direct_tcp=True)

conn.connect(server_ip, 445)

c = consul.Consul(host='consul', port=8500)  # Reemplaza con la dirección y puerto correctos

check = consul.Check.http('http://fastapi:8000/health', interval='55s')
c.agent.service.register('fast-api', port=8000, check=check)


@app.get("/health")
async def health():
    return {"status": "OK"}


@app.get("/")
async def root():
    return {"message": "Hello World"}

@app.get("/blob/list")
async def list_items():
    files = [x.filename for x in conn.listPath(
        share_name, path='/') if not x.filename.startswith('.')]
    return files

@app.get("/blob/remove/{item_name}")
async def remove_item(item_name):
    files = conn.deleteFiles(share_name, item_name)
    return files

# Upload files
@app.post("/blob/upload")
async def write_item(file: UploadFile ,  response: Response):
    file_name = file.filename

    file_exists = any(x == file_name for x in (await list_items()))
    if file_exists:
        response.status_code = 400
        return {"detail": "file already exists"}

    conn.storeFile(share_name, '/' + file_name, file.file)
    return {"detail": "ok"}

@app.post("/blob/update")
async def write_item(file: UploadFile, response: Response):
    file_name = file.filename

    file_exists = any(x == file_name for x in (await list_items()))
    if not file_exists:
        response.status_code = 400
        return {"detail": "Cannot update, file doesn't exist"}

    await remove_item(file_name)

    conn.storeFile(share_name, '/' + file_name, file.file)
    return {"detail": "ok"}

@app.on_event("shutdown")
async def shutdown_event():
    conn.close()
    c.agent.service.deregister(consul)

