from ..repository import Libro
from ..repository.mongo import MongoRepository
from .schema import KudoSchema

class Service(object):
 def __init__(self, user_id, libro_client=Libro(adapter=MongoRepository)):
   self.libro_client = libro_client
   self.user_id = user_id

   if not user_id:
     raise Exception("EL MALPARIDO ID del usuario no fue")

 def find_all_ebooks(self):
   kudos  = self.libro_client.find_all({'user_id': self.user_id})
   return [self.dump(ebook) for ebook in ebooks]

 def find_ebook(self, ebook_id):
   kudo = self.libro_client.find({'user_id': self.user_id, 'ebook_id': ebook_id})
   return self.dump(kudo)

 def create_ebook_for(self, ebook):
   self.libro_client.create(self.prepare_ebook(ebook))
   return self.dump(ebook.data)

 def update_ebook_with(self, ebook_id, ebook):
   records_affected = self.libro_client.update({'user_id': self.user_id, 'ebook_id': ebook_id}, self.prepare_ebook(ebook))
   return records_affected > 0

 def delete_ebook_for(self, ebook_id):
   records_affected = self.libro_client.delete({'user_id': self.user_id, 'ebook_id': ebook_id})
   return records_affected > 0

 def dump(self, data):
   return KudoSchema(exclude=['_id']).dump(data).data

 def prepare_book(self, ebook):
   data = ebook.data
   data['user_id'] = self.user_id
   return data
