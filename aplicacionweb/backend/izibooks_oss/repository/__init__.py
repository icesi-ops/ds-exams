class Libro(object):
 def __init__(self, adapter=None):
   self.client = adapter()

 def find_all(self, selector):
   return self.client.find_all(selector)
 
 def find(self, selector):
   return self.client.find(selector)
 
 def create(self, ebook):
   return self.client.create(ebook)
  
 def update(self, selector, ebook):
   return self.client.update(selector, ebook)
  
 def delete(self, selector):
   return self.client.delete(selector)
