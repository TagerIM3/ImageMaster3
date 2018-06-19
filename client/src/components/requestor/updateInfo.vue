<template>
  <div>
    <Content :style="{padding: '0 50px'}">
      <Breadcrumb :style="{margin: '20px 0'}">
        <BreadcrumbItem></BreadcrumbItem>
      </Breadcrumb>

      <div style="min-height: 200px;padding-left: 0px">

        <Card style="width:500px;height:550px;" >
          <p>用户名：</p><p>{{username}}</p>
          <!--<Form  ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" :label-position="left" style="padding-right: 20px">-->
            <!--<FormItem label="选择头像" prop="image">-->
              <!--<div style="padding-left: 70px">-->
                <!--<uploadFile ref = "upload"></uploadFile>-->
              <!--</div>-->
            <!--</FormItem>-->

            <!--<FormItem label="地址" prop="address">-->
              <!--<i-input clearable type="text" placeholder="请输入地址" v-model="formValidate.Address" ></i-input>-->
              <!--&lt;!&ndash;<i-input clearable type="text" placeholder="请输入预期参与人数" v-model="people" ></i-input>&ndash;&gt;-->
            <!--</FormItem>-->
            <!--<FormItem label="电话" prop="phone">-->
              <!--<i-input clearable type="text" placeholder="请输入地址" v-model="formValidate.Phone" ></i-input>-->
            <!--</FormItem>-->
            <!--<FormItem label="个人介绍" prop="intro">-->
              <!--<i-input clearable type="textArea" placeholder="请输入个人介绍" v-model="formValidate.Intro" ></i-input>-->
            <!--</FormItem>-->
            <!--<FormItem>-->
              <!--<Poptip trigger="hover" title="提示" content="确认输入信息完整后点击" placement="top" style="padding-left: 50px">-->
                <!--<i-button type="primary" icon="checkmark-round" align="center" @click="submit('formValidate')">修改信息</i-button>-->
              <!--</Poptip>-->
              <!--<Poptip trigger="hover" title="提示" content="清除已输入的所有内容" placement="top"  style="padding-left: 50px">-->
                <!--<i-button type="ghost" icon="close-round"  @click="reset('formValidate')">清除输入</i-button>-->
              <!--</Poptip>-->
            <!--</FormItem>-->
          <!--</Form>-->

          <p slot="title">
            <Icon type="ios-film-outline"></Icon>
            个人信息
          </p>
          <!--<ul>-->

          <!--</ul>-->
        </Card>

      </div>

    </Content>
  </div>
</template>

<script>
  import  uploadFile from '../util/avatarUpload'
  import api from '../../api/requestor'
    export default {
        name: "updateInfo",
      components: {uploadFile },
      data () {
        return {
          formValidate: {
            Address: '',
            Phone: '',
            Intro:''
          },
          ruleValidate: {

          },
          username: '',
        }
      },
      methods:{
        show(){

        },
        submit(name) {
          this.$refs[name].validate((valid) => {
            if (1) {
              let taskPre = {
                'phone': this.formValidate.Address,
                'address': this.formValidate.Phone,
                'introduction': this.formValidate.Intro,
              };
              let data = {
                'file':this.$refs.upload.file
              }
              console.log(data)
              api.modify(taskPre,(res) => {
                  //console.log(res)
                  if (res === 'SUCCESS') {
                    if(this.$refs.upload.file)api.modifyAvatar(data,
                      (res)=>{
                        if (res === 'SUCCESS') {
                          this.$Message.success('头像上传成功')
                          this.$refs.upload.file = null
                        }
                        else if (res === 'WRONG') {
                          this.$Message.error('网络错误，头像上传失败')
                        } else if (res === 'FAIL') {
                          this.$Message.error('网络错误，头像上传失败')
                        }
                        else if (res === 'TIMEOUT') {
                          this.$Message.error('您因为太久未操作而被登出！');
                          this.$router.push({
                            path: '/',
                          });
                        }
                        else{
                          this.$Message.error('请确认选择的文件是否有误');
                        }
                      },
                      (err)=>{
                        this.$Message.error(err);
                      }
                    )
                    this.$Message.success("个人信息修改成功！");
                  }
                  else if (res === 'TIMEOUT'){
                    this.$Message.error('您因为太久未操作而被登出！')
                    this.$router.push({
                      path: '/',
                    });
                  }
                  else if (res === 'FAIL') {
                    this.$Message.error('修改信息失败，请稍后再试')
                  }
                  else{
                    this.$Message.error('修改信息失败，请稍后再试')
                  }
                },
                (err) => {
                  this.$Message.error(err.message);
                })

            }
            else{
              this.$Message.error('请检查输入是否有误');
            }
          })
        },

        reset(name) {

          this.$refs[name].resetFields();
          this.$refs.upload.file = null;

        },
      }
    }
</script>

<style scoped>

</style>
