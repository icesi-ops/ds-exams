const express = require("express");
const cors = require("cors");
const multer = require("multer");
const fs = require("fs");
const SambaClient = require('samba-client');

let client = new SambaClient({
  address: '//samba/storage', // required
  username: 'backend', // not required, defaults to guest
  password: 'backend', // not required
  domain: 'WORKGROUP', // not required
  maxProtocol: 'SMB3', // not required
  maskCmd: true, // not required, defaults to false
});

console.log("SAMBA CLIENT:", client);

const app = express();

app.use(cors());

UPLOAD_PATH = "storage"; 

const storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, UPLOAD_PATH)
    },
    filename: function (req, file, cb) {
      cb(null, file.originalname)
    }
})

const upload = multer({ storage: storage })

const PORT = '5000' || process.env.PORT;

async function send_file_samba(src, dst) {
  console.log("Sending file to sambda.. from ", src, " to ", dst);
  var res = await client.sendFile(src, dst);
  console.log("Done");
  console.log(res);
}

app.post("/api/upload", upload.single("file"), (req, res) => {
    console.log(req.file);
    send_file_samba(req.file.path, req.file.filename);
    res.json({file: req.file});
});

app.listen(PORT, () => console.log("Server listening on port " + PORT));
