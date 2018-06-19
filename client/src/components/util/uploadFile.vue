<!--<template>-->
  <!--<Upload-->
    <!---->
    <!--t-->
    <!--action="//jsonplaceholder.typicode.com/posts/">-->
    <!--<div style="padding: 20px 0">-->
      <!--<Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>-->
      <!--<p></p>-->
    <!--</div>-->
  <!--</Upload>-->
<!--</template>-->
<!--<script>-->
  <!--export default {-->

  <!--}-->
<!--</script>-->

<template>
  <div>
    <Upload
      :format="['zip']"
      :before-upload="handleUpload"
      type="drag"

      action="http://106.14.178.184:8080/im3/requestor/uploadImages">
      <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
      <p>拖拽或选择压缩包后上传</p>
    </Upload>
    <Button type="text" @click="upload" disabled :loading="loadingStatus">{{ loadingStatus ? 'Uploading' : '' }}</Button>


    <div v-if="file !== null">Upload file: {{ file.name }} </div>
  </div>
</template>
<script>
  export default {
    data () {
      return {
        file: null,
        loadingStatus: false,
        uploading:true
      }
    },
    methods: {
      handleUpload (file) {
        this.file = file;
        this.$Notice.open({
          title: '提示',
          desc: '压缩包必须为ZIP格式，大小不能超过100MB，只能读取压缩包根目录下的图片！',
          duration: 0
        });
        return false;
      },
      upload () {

          this.loadingStatus = true;



        // api.uploadImages(param,(res) => {
        //     console.log(res)
        //     if (res === 'SUCCESS') {
        //       this.$Message.success("任务信息已提交，正在上传压缩包，请勿切换界面");
        //       this.reset('formValidate');
        //     }
        //
        //     else if (res === 'FAIL') {
        //       this.$Message.error('提交任务失败，请稍后再试')
        //     }
        //     else{
        //
        //     }
        //   },
        //   (err) => {
        //     console.log(err)
        //     this.$Message.error(err.message);
        //   })


        setTimeout(() => {
            this.file = null;
            this.loadingStatus = false;
            this.uploading = false;
            this.$Message.success('压缩包上传成功！')

          }, 1500);


      },


    }
  }
</script>
