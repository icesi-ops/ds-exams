from marshmallow import Schema, fields

class EbooksSchema(Schema):
  id = fields.Int(required=True)
  book_name = fields.Str()
  author = fields.Str()
  editorial = fields.Str()
  genre = fields.Str()
  ebook_url = fields.URL()

class KudoSchema(EbooksSchema):
  user_id = fields.Email(required=True)
