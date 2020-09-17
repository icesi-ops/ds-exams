from .middlewares import login_required
from flask import Flask, json, g, request
from app.izibooks.service import Service as Libro
from app.izibooks.schema import EbooksSchema
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route("/ebooks", methods=["GET"])
@login_required
def index():
 return json_response(Librosg.user).find_all_ebooks()


@app.route("/ebooks", methods=["POST"])
@login_required
def create():
   github_repo = EbooksSchema().load(json.loads(request.data))
  
   if github_repo.errors:
     return json_response({'error': ebooks_repo.errors}, 422)

   libro = Libro(g.user).create_ebook_for(ebooks_repo)
   return json_response(libro)


@app.route("/ebooks/<int:ebook_id>", methods=["GET"])
@login_required
def show(ebook_id):
 libro = Libro(g.user).find_ebook(ebook_id)

 if libro:
   return json_response(libro)
 else:
   return json_response({'error': 'Libro no encontrado'}, 404)


@app.route("/ebooks/<int:ebook_id>", methods=["PUT"])
@login_required
def update(ebook_id):
   ebooks_repo = EbooksSchema().load(json.loads(request.data))
  
   if ebooks_repo.errors:
     return json_response({'error': github_repo.errors}, 422)

   libro_service = Libro(g.user)
   if kudo_service.update_ebook_with(ebook_id, ebooks_repo):
     return json_response(ebooks_repo.data)
   else:
     return json_response({'error': 'Libro no encontrado'}, 404)

  
@app.route("/ebooks/<int:ebook_id>", methods=["DELETE"])
@login_required
def delete(repo_id):
 libro_service = Libro(g.user)
 if libro_service.delete_ebook_for(ebook_id):
   return json_response({})
 else:
   return json_response({'error': 'Libro no encontrado'}, 404)


def json_response(payload, status=200):
 return (json.dumps(payload), status, {'content-type': 'application/json'})
