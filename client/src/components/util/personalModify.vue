<template>
  <div>
    <Content :style="{padding: '0 50px'}">
      <Breadcrumb :style="{margin: '20px 0'}">
        <BreadcrumbItem></BreadcrumbItem>
      </Breadcrumb>

      <div style="min-height: 200px;padding-left: 0%">

        <Card style="min-height:550px">
          <p slot="title">
            <Icon type="ios-film-outline"></Icon>
            个人信息
          </p>
          <div style="padding-left:25%;padding-right: 35%;">
            <div>
              <p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;用户名：</p>
              <p style="">{{username}}</p><br>
            </div>
            <div>
              <p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;邮箱：</p>
              <p style="">{{email}}</p><br>
            </div>

            <!--<al-selector v-model="res_s" style="width: 40%" level="1" @on-change="save"/><br>-->
            <div>
              <div style="float: left;width:20%;height:10px">
                <div style="position: relative;padding-top: 12%">
                  <p style="float: left;">所在地区：</p>
                </div>

              </div>

              <div style="width: 50%;position: relative;left:18.75%">
                <al-cascader v-model="area" data-type="name" @on-change="showArea" level="1"/>
                <!--<al-selector v-model="area" data-type="name" level="1"></al-selector>-->
                <br>
              </div>

            </div>

            <!--<cascaderMulti :data="res2" placeholder="状态码"></cascaderMulti>-->
            <div>
              <p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;微信号：</p>
              <i-input style="width:50%" v-model="wechat"></i-input>
            </div>
            <br>
            <div>
              <p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;手机号：</p>
              <i-input style="width:50%" v-model="phone"></i-input>
            </div>
            <br>
            <div>
              <p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;地址：</p>
              <i-input style="width:50%" v-model="address"></i-input>
            </div>
            <br>
            <div>
              <p style="float: left;">自我介绍：</p>
              <i-input style="width:50%;height:70px" v-model="introduction" type="textarea"></i-input>
            </div>
            <br>

            <div style="padding-left: 0px;width:40%">
              <p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;头像：</p>
              <!--<uploadFile ref = "upload"></uploadFile>-->
              <a style="" @click="modalAvatar = true">修改头像</a>
            </div>
            <br>
            <Button type="primary" style="position: relative;left:20%;" @click="saveMessage = true">保存</Button>
          </div>

          <Modal
            v-model="modalAvatar"
            title="修改头像"
            @on-ok=""
            @on-cancel="">

            <div style="width: 30%;float:left">
              <p style="font-size: 15px">上传我的新头像：</p>
            </div>
            <div style="width: 60%">
              <!--<uploadFile ref = "upload"></uploadFile>-->
              <upload-camera ref="upload"></upload-camera>
            </div>

          </Modal>

          <Modal
            v-model="saveMessage"
            title="保存修改"
            @on-ok="save"
            @on-cancel="">
            <p>确定保存个人信息？</p>

          </Modal>

          <!--<Form  ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" :label-position="left" style="padding-right: 20px">-->

          <!--<FormItem label=" 用户名：" prop="image">-->
          <!--<div>-->
          <!--&lt;!&ndash;<p style="float: left;">&nbsp;&nbsp;&nbsp;&nbsp;用户名：</p>&ndash;&gt;-->
          <!--<p style="">{{username}}</p>-->
          <!--</div>-->
          <!--</FormItem>-->

          <!--<FormItem label="    邮箱：" prop="image">-->
          <!--<div>-->
          <!--<p style="">{{email}}</p>-->
          <!--</div>-->
          <!--</FormItem>-->

          <!--<FormItem label="所在地区：" prop="image">-->
          <!--<div style="width: 30%">-->
          <!--<al-cascader v-model="res_c" data-type="all" @on-change="save" level="1"/><br>-->
          <!--</div>-->
          <!--</FormItem>-->

          <!--<FormItem label=" 微信号：" prop="image">-->
          <!--<div>-->
          <!--<i-input style="width:30%" v-model="wechat"></i-input>-->
          <!--</div>-->
          <!--</FormItem>-->

          <!--</Form>-->

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

          <!--<p slot="title">-->
          <!--<Icon type="ios-film-outline"></Icon>-->
          <!--个人信息-->
          <!--</p>-->
        </Card>

      </div>

    </Content>
  </div>
</template>

<script>
  import uploadFile from './avatarUpload'
  import api from '../../api/requestor'
  import uploadCamera from './uploadCamera'
  import workerAPI from '../../api/worker'
  import ResultMessage from '../../util/ResultMessage'

  export default {
    components: {uploadFile, uploadCamera},
    data() {
      return {
        formValidate: {
          Address: '',
          Phone: '',
          Intro: ''
        },
        ruleValidate: {},
        username: '',
        email: '',
        area: [],
        wechat: '',
        phone: '',
        address: '',
        introduction: '',
        modalAvatar: false,
        saveMessage: false,
        // flag: false,
      }
    },
    mounted() {
      this.showInfo();
    },
    methods: {
      showArea() {
        // this.$Message.success(this.area[1]);
      },
      showInfo() {
        workerAPI.getWorker((res) => {
            if (res.message === ResultMessage.SUCCESS) {
              let data = res.object;
              this.username = data.username;
              this.email = data.email;
              // this.area[0] = data.province;
              // this.area[1] = data.city;
              this.wechat = data.wechat;
              this.phone = data.phone;
              this.address = data.address;
              this.introduction = data.introduction;

              this.$set(this.area,0,data.province);
              this.$set(this.area,1,data.city);

              // this.flag = true;
            }
            else if (res.message === ResultMessage.TIMEOUT) {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.$Message.error('网络错误，请稍后再试');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          });
      },
      save() {
        let thisArea = false, thisWechat = false, thisTel = false, thisAddress = false, thisIntro = false;
        thisArea = (this.area === null);
        // thisWechat= (this.wechat === '');
        // thisTel = (this.phone === '');
        if (this.wechat === '') {
          thisWechat = true;
        } else if (this.wechat.indexOf(' ') !== -1) {
          thisWechat = true;
        }

        if (this.phone === '') {
          thisWechat = true;
        } else if (this.phone.indexOf(' ') !== -1) {
          thisWechat = true;
        }
        thisAddress = (this.address === '');
        thisIntro = (this.introduction === '');

        // if (thisArea || thisWechat || thisTel || thisAddress || thisIntro) {
        //   this.$Message.error('请正确填写个人信息');
        // } else {
        let userPre = {
          'province': this.area[0],
          'city': this.area[1],
          'phone': this.phone,
          'address': this.address,
          'wechat': this.wechat,
          'introduction': this.introduction
        };
        let avatarFile = {
          'file': this.$refs.upload.file
        }
        // console.log(avatarFile)
        api.modify(userPre, (res) => {
            if (res === 'SUCCESS') {
              if(this.$refs.upload.file !== null){
                if (this.$refs.upload.file) api.modifyAvatar(this.$refs.upload.file,
                  (res) => {
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
                    else {
                      this.$Message.error('请确认选择的文件是否有误');
                    }
                  },
                  (err) => {
                    this.$Message.error(err);
                  }
                )
              }

              this.$Message.success("个人信息修改成功！");
            }
            else if (res === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.$Message.error('修改信息失败');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          });
      // }
      },
      show() {

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
              'file': this.$refs.upload.file
            }
            console.log(data)
            api.modify(taskPre, (res) => {
                //console.log(res)
                if (res === 'SUCCESS') {
                  if (this.$refs.upload.file) api.modifyAvatar(data,
                    (res) => {
                      if (res === 'SUCCESS') {
                        this.$Message.success('头像上传成功');
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
                      else {
                        this.$Message.error('请确认选择的文件是否有误');
                      }
                    },
                    (err) => {
                      this.$Message.error(err);
                    }
                  )
                  this.$Message.success("个人信息修改成功！");
                }
                else if (res === 'TIMEOUT') {
                  this.$Message.error('您因为太久未操作而被登出！')
                  this.$router.push({
                    path: '/',
                  });
                }
                else if (res === 'FAIL') {
                  this.$Message.error('修改信息失败，请稍后再试')
                }
                else {
                  this.$Message.error('修改信息失败，请稍后再试')
                }
              },
              (err) => {
                this.$Message.error(err.message);
              })

          }
          else {
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
