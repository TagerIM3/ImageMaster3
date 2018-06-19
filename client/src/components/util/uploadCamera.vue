<template>
    <div>
      <!--<Upload-->
        <!--ref="upload"-->
        <!--:show-upload-list="false"-->
        <!--:default-file-list="defaultList"-->
        <!--:on-success="handleSuccess"-->
        <!--:format="['jpg','jpeg','png']"-->
        <!--:max-size="2048"-->
        <!--:on-format-error="handleFormatError"-->
        <!--:on-exceeded-size="handleMaxSize"-->
        <!--:before-upload="handleBeforeUpload"-->
        <!--multiple-->
        <!--type="drag"-->
        <!--action=""-->
        <!--style="display: inline-block;width:58px;">-->
        <!--<div style="width: 58px;height:58px;line-height: 58px;">-->
          <!--<Icon type="camera" size="20"></Icon>-->
        <!--</div>-->
      <!--</Upload>-->

      <Upload
        :before-upload="handleUpload"
        :show-upload-list="uop"
        accept="image/jpeg,image/jpg,image/png"
        :format="['jpg','jpeg','png']"
        :on-success="handleSuccess"
        :on-format-error="handleFormatError"
        :on-error="handleError"
        type="drag"
        action="http://106.14.178.184:8080/im3/requestor/uploadImages"
        style="display: inline-block;width:58px;">
        <div style="width: 58px;height:58px;line-height: 58px;">
          <Icon type="camera" size="20"></Icon>
        </div>
      </Upload>
      <!--<Button type="text" @click="upload" disabled :loading="loadingStatus">{{ loadingStatus ? 'Uploading' : '' }}</Button>-->
      <div v-if="file !== null">Upload file: {{ file.name }} </div>
    </div>
</template>

<script>
  export default {
    data () {
      return {
        file: null,
        loadingStatus: false,
        uploading:true,
        uop:false
      }
    },
    methods: {
      handleUpload (file) {
        this.file = file;
        console.log(file)
        return 1;
      },
      handleSuccess(res,file,list){
        let data = {}
        data['file'] = this.file

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

    }
  }
</script>

<style scoped>

</style>
