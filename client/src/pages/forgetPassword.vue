<template>
  <div class="forgetBg">
    <Card id="forget">
      <p slot="title">修改密码</p>
      <Form ref="formInline" :model="formInline" :rules="ruleInline" inline>
        <!--<FormItem prop="newUsername">-->
        <!--<i-input type="text" v-model="formInline.newUsername" placeholder="用户名" style="width: 300px">-->
        <!--<Icon type="ios-person-outline" slot="prepend"></Icon>-->
        <!--</i-input>-->
        <!--</FormItem>-->
        <br>
        <FormItem prop="newEmail">
          <i-input type="text" v-model="formInline.email" placeholder="邮箱" style="width: 300px">
            <Icon type="email" slot="prepend"></Icon>
          </i-input>
        </FormItem>
        <br>
        <FormItem prop="newPassword">
          <i-input type="password" v-model="formInline.newPassword" placeholder="新密码" style="width: 300px">
            <Icon type="ios-locked-outline" slot="prepend"></Icon>
          </i-input>
        </FormItem>
        <br>

        <FormItem prop="newCheck">
          <i-input type="text" v-model="formInline.newCheck" placeholder="请输入验证码" style="width: 160px"></i-input>
          <Button @click="countDown" type="primary" class="Button" :class="{disabled: !this.canClick}">{{content}}
          </Button>
        </FormItem>
        <Button type="primary" @click="handleSubmit('formInline')" style="width: 300px"
                :class="{disabled: !this.canClick2}">{{contentOfButton}}
        </Button>
      </Form>
      <br>
      <a @click="ToLogin">返回首页</a>
    </Card>
  </div>
</template>

<script>
  import remoteLoc from '../util/remote.js'
  import userAPI from '../api/user'

  var passwordChecker = /[`~!@#$%^&*()_+<>?:"{},.;'[]/im
  var mailChecker = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
  export default {
    name: "forgetPassword",
    data() {
      // const validateUsername = (rule, value, callback) => {
      //   if (value === '') {
      //     callback(new Error('用户名不能为空'));
      //   }
      //   else if (passwordChecker.test(value) || value.indexOf(" ") > -1) {
      //     callback(new Error('用户名含有非法字符'));
      //   } else {
      //     callback();
      //   }
      // };
      const validatePassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('密码不能为空'));
        }
        else if (passwordChecker.test(value) || value.indexOf(" ") > -1) {
          callback(new Error('密码含有非法字符'));
        }
        else if (value.length < 6) {
          callback(new Error('密码长度不能少于6位'));
        }
        else {
          callback();
        }
      };
      const validateEmail = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('邮箱不能为空'));
        }
        else if (!mailChecker.test(value)) {
          // alert(this.formInline.email);
          callback(new Error('邮箱格式错误'));
        } else {
          callback();
        }
      };
      const validateCheck = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入验证码'));
        }
        else {
          callback();
        }
      };

      return {
        content: '发送验证码',
        totalTime: 60,
        canClick: true, //添加canClick
        contentOfButton: '立即修改',
        pushTime: 2,
        canClick2: true,
        formInline: {
          EmailCache: '',
          newUsername: '',
          newPassword: '',
          email: '',
          newCheck: ''
        },
        ruleInline: {
          // newUsername: [
          //   // {required: true, message: '用户名不能为空', trigger: 'blur'}
          //   {validator: validateUsername, trigger: 'blur'}
          // ],
          newPassword: [
            // { required: true, message: '密码不能为空', trigger: 'blur' },
            // { type: 'string', min: 6, message: '密码长度不得少于6位', trigger: 'blur' }
            {validator: validatePassword, trigger: 'blur'}
          ],
          email: [
            {validator: validateEmail, trigger: 'blur'}
          ],
          newCheck: [
            {validator: validateCheck, trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      ToLogin() {
        this.$router.push('/');
      },
      countDown() {
        this.sendEmail();
        if (!this.canClick) return;  //改动的是这两行代码
        this.canClick = false;
        this.content = this.totalTime + 's后重新发送';
        let clock = window.setInterval(() => {
          this.totalTime--;
          this.content = this.totalTime + 's后重新发送';
          if (this.totalTime < 0) {
            window.clearInterval(clock);
            this.content = '发送验证码';
            this.totalTime = 60;
            this.canClick = true  //这里重新开启
          }
        }, 1000)
      },
      sendEmail() {
        this.formInline.EmailCache = this.formInline.email;
        var mode = 1;
        userAPI.send(this.formInline.email, mode, (res) => {
            if (res === 'SUCCESS') {
              // this.showTip_8 = true;
              // this.tip_8 = '验证码已发送致' + this.EmailCache + '!'
              this.$Message.success('验证码已发送至' + this.formInline.EmailCache);
            }
            else if (res === 'NOT_EXIST') {
              // this.showTip_8 = true;
              // this.tip_8 = res.body.object
              this.$Message.error('邮箱未被注册');
            } else if (res === 'FAIL') {
              this.$Message.error('验证码发送失败')
            }
          },
          (err) => {
            this.$Message.error(err.message)
          }
        )
        // adminAPI.modify(userPre,
        //   (response) => {
        //     if (response.body.code === 0 && response.body.object === true) {
        //
        //       // this.$Message.success('密码修改成功');
        //       // this.$router.push('/');
        //     }else{
        //       this.$Message.error(response.body.object);
        //     }
        //   },
        //   (err) => {
        //   this.$Message.error('网络出错');
        //   }
        // );
        // adminAPI.isUsedEmail(this.formInline.email,
        //
        //   (response) => {
        //     if (response.body.code === 0 && response.body.object === false) {
        //       adminAPI.send(this.formInline.email, (res) => {
        //
        //         if (res.body.code === 0) {
        //           // this.showTip_8 = true;
        //           // this.tip_8 = '验证码已发送致' + this.EmailCache + '!'
        //           this.$Message.success('验证码已发送致' + this.formInline.EmailCache);
        //         }
        //         else {
        //           // this.showTip_8 = true;
        //           // this.tip_8 = res.body.object
        //           this.$Message.error(res.body.object);
        //         }
        //       }, (err) => {
        //         // this.showTip_8 = true;
        //         // this.tip_8 = '网络错误，获取验证码失败！'
        //         this.$Message.error('网络错误，获取验证码失败');
        //       })
        //     }
        //     else {
        //       // this.showTip_8 = true;
        //       // this.tip_8 = '该邮箱已被使用！'
        //       this.$Message.error('该邮箱已被使用');
        //     }
        //
        //
        //   },
        //
        //   (err) => {
        //     // this.showTip_8 = true;
        //     // this.tip_8 = '网络错误，获取验证码失败！'
        //     this.$Message.error('该邮箱已被使用');
        //   }
        // )
      },
      handleSubmit(name) {
        var userPre = {
          'username_email': this.formInline.newUsername,
          'password': this.formInline.newPassword,
          'email': this.formInline.email,
          'code': this.formInline.newCheck
        };
        this.$refs[name].validate((valid) => {
          if (valid) {
            //this.$Message.success('Success!');
            if (this.formInline.EmailCache === this.formInline.email) {
              userAPI.modify(userPre,
                (response) => {
                  if (response === 'SUCCESS') {

                    this.$Message.success('密码修改成功');
                    this.canClick2 = false;
                    this.content = this.pushTime + 's后回到首页';
                    let clock = window.setInterval(() => {
                      this.totalTime--;
                      this.content = this.totalTime + 's后回到首页';
                      if (this.totalTime < 0) {
                        window.clearInterval(clock);
                        this.$router.push('/');
                      }
                    }, 1000)
                    // this.$router.push('/');
                  } else if (res === 'WRONG') {
                    this.$Message.error('验证码错误');
                  }
                  else if (res === 'FAIL') {
                    this.$Message.error('修改失败');
                  }
                  else {
                    this.$Message.error(response);
                    this.$Message.success('您已成功修改密码');
                  }
                },
                (err) => {
                  this.$Message.error(err.message);
                }
              );
            }
            else {
              this.$Message.error('当前输入邮箱与获取验证码的邮箱不相同');
            }

          } else {
            this.$Message.error('请输入正确的修改信息');
          }
        })
      }
    }
  }
</script>

<style scoped>
  #forget {
    position: relative;
    left: 68%;
    top: 20%;
    /*width: 26%;*/
    width: 330px;
  }

  .forgetBg {
    background-size: cover;
    background-image: url('../assets/background.jpg');
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    height: 100%;
  }
</style>
