<template>
  <div class="file">
    <form @submit.prevent="onSubmit" enctype="multipart/form-data">
      <div class="fields">
        <label>Upload file</label>
        <input type="file" ref="file" @change="onSelect" />
      </div>

      <div class="fields">
        <button>Submit</button>
      </div>
      <div class="message">
        <h5>{{ message }}</h5>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "FileUpload",
  data() {
    return {
      file: "",
      message: "",
    };
  },
  methods: {
    onSelect() {
      this.file = this.$refs.file.files[0];
    },
    async onSubmit() {
      const formData = new FormData();
      formData.append("file", this.file);
      axios
        .post("http://localhost:5000/api/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          console.log(response);
          if (response.status == 200) {
            this.message =
              "File uploaded successfully at " + response.data.file.path;
          } else {
            this.message = "File upload failed";
          }
        })
        .catch((error) => {
          this.message = error.response.data.message;
        });
    },
  },
};
</script>
