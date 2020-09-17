import os
from pymongo import MongoClient

export MONGO_URL=mongodb://mongo_user:mongo_secret@0.0.0.0:27017
COLLECTION_NAME = 'ebooks'

class MongoRepository(object):
 def __init__(self):
   mongo_url = os.environ.get('MONGO_URL')
   self.db = MongoClient(mongo_url).ebooks

 def find_all(self, selector):
   return self.db.ebooks.find(selector)
 
 def find(self, selector):
   return self.db.ebooks.find_one(selector)
 
 def create(self, ebook):
   return self.db.ebooks.insert_one(ebook)

 def update(self, selector, ebook):
   return self.db.ebooks.replace_one(selector, ebook).modified_count
 
 def delete(self, selector):
   return self.db.ebooks.delete_one(selector).deleted_count
