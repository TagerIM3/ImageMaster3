<template>
  <Upload
    multiple
    type="drag"
    :show-upload-list="up"
    accept="image/jpeg,image/jpg,image/png"
    :format="['jpg','jpeg','png']"
    :before-upload="handleUpload"
    :on-success="handleSuccess"
    :on-format-error="handleFormatError"
    :on-error="handleError"
    :max-size="1"
    :on-exceeded-size="sizeError"

    action="http://localhost:8081/requestor/uploadAvatar">


    <div style="padding: 20px 0">
      <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
      <p>点击选择或拖拽文件至此</p>
      <div v-if="file !== null">Upload file: {{ file.name }} </div>
    </div>
  </Upload>
</template>
<script>
  import api from "../../api/requestor"
  export default {
    data () {
      return {
        file: null,
        loadingStatus: false,
        uploading:false,
        up:false
      }
    },
    methods: {
      handleUpload (file) {
        this.file = file;
        console.log(this.file)
        return false;
      },
      handleSuccess(res,file,list){
        let data = {}
        data['file'] = this.file
        console.log(123)

       // console.log(res)
      },
      handleError(err){
       //console.log(err)
      },
      handleFormatError (file) {
        this.$Notice.warning({
          title: '文件格式错误',
          desc: '' + file.name + '格式错误,请选择jpg 或者 png格式的图片.'
        });
      },
      sizeError(){
        this.$Notice.warning({
          title: '文件超出规定大小',
          desc: '' + file.name + '文件超出规定大小'
        });
      }
    }
  }
</script>
