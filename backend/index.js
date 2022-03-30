const express = require("express");
const cors = require("cors");
const multer = require("multer");
const fs = require("fs");

const app = express();

app.use(cors());

UPLOAD_PATH = process.env.HOME + '/ds-parcial-1-storage'; 

console.log("Creating directory " + UPLOAD_PATH);
fs.mkdirSync(UPLOAD_PATH, { recursive: true })

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


app.post("/api/upload", upload.single("file"), (req, res) => {
    console.log(req.file);
    res.json({file: req.file});
});

app.listen(PORT, () => console.log("Server listening on port " + PORT));
