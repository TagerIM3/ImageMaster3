<template>
  <!--<div v-on:click="showTips">-->


  <!--<div class="register-wrap">-->
  <!--<h3>注册</h3>-->

  <!--<input type="text" placeholder="请输入用户名" v-model="newUsername">-->
  <!--<p v-show="showTip_4">{{tip_4}}</p>-->
  <!--<input type="password" placeholder="请输入密码" v-model="newPassword">-->
  <!--<p v-show="showTip_5">{{tip_5}}</p>-->
  <!--<input type="password" placeholder="确认密码" v-model="confirmPassword">-->
  <!--<p v-show="showTip_6">{{tip_6}}</p>-->
  <!--<input type="text" placeholder="请输入邮箱" v-model="email">-->
  <!--<p v-show="showTip_7">{{tip_7}}</p>-->
  <!--<div ><button  v-on:click="sendEmail">获取验证码</button><input type="text" placeholder="请输入验证码" v-model="newCheck"></div>-->
  <!--<p v-show="showTip_8">{{tip_8}}</p>-->
  <!--<button v-on:click="register">注册</button>-->
  <!--<p v-show="showTip_9">{{tip_9}}</p>-->
  <!--<span v-on:click="ToLogin">已有账号？马上登录</span>-->
  <!--</div>-->
  <!--</div>-->
  <div class="registerBg">
  <Card id="reg" v-if="refresh">
    <p slot="title">注册</p>
    <Form ref="formInline" :model="formInline" :rules="ruleInline" inline>
      <FormItem prop="newUsername">
        <i-input type="text" v-model="formInline.newUsername" placeholder="用户名" style="width: 300px">
          <Icon type="ios-person-outline" slot="prepend"></Icon>
        </i-input>
      </FormItem>
      <br>
      <FormItem prop="newPassword">
        <i-input type="password" v-model="formInline.newPassword" placeholder="密码" style="width: 300px">
          <Icon type="ios-locked-outline" slot="prepend"></Icon>
        </i-input>
      </FormItem>
      <br>


      <!--<RadioGroup v-model="animal">-->
      <!--<Radio label="金斑蝶"></Radio>-->
      <!--<Radio label="爪哇犀牛"></Radio>-->
      <!--<Radio label="印度黑羚"></Radio>-->
      <!--</RadioGroup><br><br>-->

      <FormItem prop="email">
        <i-input type="text" v-model="formInline.email" placeholder="邮箱" style="width: 300px">
          <Icon type="email" slot="prepend"></Icon>
        </i-input>
      </FormItem>
      <br>
      <FormItem prop="newCheck">
        <i-input type="text" v-model="formInline.newCheck" placeholder="请输入验证码" style="width: 160px"></i-input>
        <Button @click="countDown" type="primary" class="Button" :class="{disabled: !this.canClick}">{{content}}
        </Button>
      </FormItem>
      <RadioGroup v-model="choseType">
        <Radio label="发起者" style="float: left"></Radio>
        <Radio label="工人" style="float: right"></Radio>
      </RadioGroup>
      <br><br>
      <Button type="primary" @click="handleSubmit('formInline')" style="width: 300px">立即注册</Button>
    </Form>
    <br>
    <a @click="ToLogin">已有账号？马上登录</a>
    <!--<div style="width: 95%">-->
    <!--<a @click="ToRegister" >已有账号？马上登录</a>-->
    <!--&lt;!&ndash;<a @click="ToForgetPassword" style="float:right">忘记密码</a>&ndash;&gt;-->
    <!--</div>-->
  </Card>
  </div>
</template>


<script>
  import userAPI from '../api/user'

  var passwordChecker = /[`~!@#$%^&*()_+<>?:"{},.;'[]/im
  var mailChecker = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
  var validUsername=false;
  var validPassword=false;
  var validEmail=false;


  export default {
    data() {
      const validateUsername = (rule, value, callback) => {
        if (value === '') {
          validUsername = false;
          callback(new Error('用户名不能为空'));
        }
        else if (passwordChecker.test(value) || value.indexOf(" ") > -1) {
          validUsername = false;
          callback(new Error('用户名含有非法字符'));
        } else {
          validUsername = true;
          callback();
        }
      };
      const validatePassword = (rule, value, callback) => {
        if (value === '') {
          validPassword = false;
          callback(new Error('密码不能为空'));
        }
        else if (passwordChecker.test(value) || value.indexOf(" ") > -1) {
          validPassword = false;
          callback(new Error('密码含有非法字符'));
        }
        else if (value.length < 6) {
          validPassword = false;
          callback(new Error('密码长度不能少于6位'));
        }
        else {
          validPassword = true;
          callback();
        }
      };
      const validateEmail = (rule, value, callback) => {
        if (value === '') {
          validEmail = false;
          callback(new Error('邮箱不能为空'));
        }
        else if (!mailChecker.test(value)) {
          // alert(this.formInline.email);
          validEmail = false;
          callback(new Error('邮箱格式错误'));
        } else {
          validEmail = true;
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
        choseType: '发起者',
        content: '发送验证码',
        totalTime: 60,
        canClick: true, //添加canClick
        refresh: true,
        formInline: {
          EmailCache: '',
          newUsername: '',
          newPassword: '',
          email: '',
          newCheck: ''
        },
        ruleInline: {
          newUsername: [
            // {required: true, message: '用户名不能为空', trigger: 'blur'}
            {validator: validateUsername, trigger: 'blur'}
          ],
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
        if (validUsername && validPassword && validEmail) {
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
        }
        else {
          this.$Message.error('请输入正确的注册信息');
        }

      },
      handleSubmit(name) {

        this.$refs[name].validate((valid) => {
          if (valid) {
            // if (this.formInline.EmailCache === this.formInline.email) {
            //
            //   this.register();
            // }
            // else {
            //   this.$Message.error('当前输入邮箱与获取验证码的邮箱不相同');
            // }
            this.register();

          } else {
            this.$Message.error('请输入正确的注册信息');
          }
        })
      },
      sendEmail() {
        this.formInline.EmailCache = this.formInline.email;
        var mode = 0;
        userAPI.send(this.formInline.email, mode, (res) => {
            if (res === 'SUCCESS') {
              // this.showTip_8 = true;
              // this.tip_8 = '验证码已发送致' + this.EmailCache + '!'
              this.$Message.success('验证码已发送至' + this.formInline.EmailCache);
            }
            else if (res === 'EXIST') {
              // this.showTip_8 = true;
              // this.tip_8 = res.body.object
              this.$Message.error('邮箱已被注册');
            } else if (res === 'FAIL') {
              this.$Message.error('验证码发送失败')
            }
          },
          (err) => {
            this.$Message.error(err.message)
          }
        )
        // var mode = 0;
        // this.formInline.EmailCache = this.formInline.email;
        // adminAPI.isUsedEmail(this.formInline.email,
        //
        //   (response) => {
        //     if (response.body.code === 0 && response.body.object === false) {
        //       adminAPI.send(this.formInline.email, mode, (res) => {
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
      register() {
        var userPre = {
          'userType': (this.choseType === '发起者') ? 'REQUESTOR' : 'WORKER',
          'username': this.formInline.newUsername,
          'password': this.formInline.newPassword,
          'email': this.formInline.email,
          'code': this.formInline.newCheck
        };

        userAPI.register(userPre,

          (res) => {
          // console.log(res)
            if (res === 'SUCCESS') {

              // this.showTip_8 = true;
              // this.tip_8 = '注册成功!'
              this.$Message.success('注册成功');
              this.$refs['formInline'].resetFields();
              // this.formInline.newUsername = '';
              // this.formInline.newPassword = '';
              // this.formInline.email = '';
              // this.formInline.newCheck = '';
              // this.formInline.EmailCache = '';
              // this.content = '获取验证码';
              // this.totalTime = -1;
              // this.canClick = true; //添加canClick
              // this.choseType = '发起者';
            }
            else if (res === 'WRONG') {
              this.$Message.error('验证码错误');
            }
            else if (res === 'EXIST') {
              // this.showTip_8 = true;
              // this.tip_8 = res.body.object
              this.$Message.error('用户名已被注册');
            }
            else if (res === 'FAIL') {
              this.$Message.error('注册失败')
            }
          },
          (err) => {
            // this.showTip_8 = true;
            // this.tip_8 = '网络错误，注册失败！'
            this.$Message.error(err.message);
          })
      }
    }


  }
  // export default{
  //   data(){
  //
  //     return{
  //       showLogin: true,
  //       showRegister: false,
  //
  //       showTip_4: false,
  //       showTip_5: false,
  //       showTip_6: false,
  //       showTip_7: false,
  //       showTip_8: false,
  //       showTip_9: false,
  //       tip_4: '',tip_5: '',tip_6: '',tip_7: '',tip_8: '',tip_9:'',
  //
  //       email:'',
  //       newCheck:'',
  //       newUsername: '',
  //       newPassword: '',
  //       confirmPassword:'',
  //       newMail:'',
  //       ifCanLogin:1,
  //       ifCanRegist:1,
  //       ifGetCheck:1,
  //       ifJump:0,
  //       EmailCache:'',
  //     }
  //   },
  //
  //   methods:{
  //
  //
  //
  //
  //     showTips(){
  //       var passwordChecker=/[`~!@#$%^&*()_+<>?:"{},.;'[]/im
  //       var mailChecker = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
  //
  //
  //       this.showTip_4 = false;
  //       this.showTip_5 = false;
  //       this.showTip_6 = false;
  //       this.showTip_7 = false;
  //       this.showTip_8 = false;
  //
  //       if(this.ifJump){
  //
  //         this.ifJump=0
  //         this.showTip_4 = false;
  //         this.showTip_5 = false;
  //         this.showTip_6 = false;
  //         this.showTip_7 = false;
  //         this.showTip_8 = false;
  //         this.showTip_9 = false
  //         return
  //       }
  //       this.ifCanLogin=1;this.ifCanRegist=1;
  //
  //
  //       //注册界面tips
  //       if(this.newUsername==""){
  //         this.showTip_4=true
  //         this.tip_4="用户名不能为空"
  //         this.ifCanRegist=0;
  //       }else if(passwordChecker.test(this.newUsername)||this.newUsername.indexOf(" ")>-1){
  //         this.showTip_4=true
  //         this.tip_4="用户名含有非法字符"
  //         this.ifCanRegist=0;
  //       }
  //
  //
  //       if(this.newPassword==""){
  //         this.showTip_5=true
  //         this.tip_5="密码不能为空"
  //         this.ifCanRegist=0;
  //       }else if(passwordChecker.test(this.newPassword)||this.newPassword.indexOf(" ")>-1){
  //         this.showTip_5=true
  //         this.tip_5="密码含有非法字符"
  //         this.ifCanRegist=0;
  //       }
  //
  //
  //       if(this.newPassword!=this.confirmPassword){
  //         this.showTip_6=true
  //         this.tip_6="两次密码不相同"
  //         this.ifCanRegist=0;
  //       }
  //
  //
  //       if(!mailChecker.test(this.email)){
  //         this.showTip_7=true
  //         this.tip_7="邮箱格式错误"
  //         this.ifCanRegist=0;
  //       }
  //
  //
  //
  //     },
  //
  //
  //
  //     ToLogin(){
  //       this.showTip_4 = false;
  //       this.showTip_5 = false;
  //       this.showTip_6 = false;
  //       this.showTip_7 = false;
  //       this.showTip_8 = false;
  //       this.showTip_9 = false
  //       this.showRegister = false
  //       this.showLogin = true
  //       this.email=''
  //       this.newCheck=''
  //       this.newUsername= ''
  //       this.newPassword= ''
  //       this.confirmPassword=''
  //       this.newMail=''
  //       this.ifJump=1;
  //
  //       this.$router.push({path:'/'})
  //
  //     },
  //
  //     register(){
  //       this.showTip_9 = false
  //       if(!this.ifCanRegist){
  //         this.showTip_9 = true;
  //         this.tip_9 = '请输入正确的注册信息!'
  //       }
  //       else if(!this.ifGetCheck){
  //         this.showTip_9 = true;
  //         this.tip_9 = '请先获取验证码!'
  //       }
  //       else if(this.newCheck==''){
  //         this.showTip_9 = true;
  //         this.tip_9 = '请输入获取的验证码!'
  //       }
  //       else{
  //
  //         if(!(this.EmailCache==this.email)){
  //           this.showTip_9 = true;
  //           this.tip_9 = '当前输入邮箱与获取验证码的邮箱不相同!'
  //
  //         }
  //         else{
  //           var userPre = {"username": this.newUsername, "password":this.newPassword, 'email' : this.email,'code' : this.newCheck};
  //
  //           adminAPI.register(this.newUsername,this.newPassword,this.email,this.newCheck,
  //
  //             (res)=>{
  //               if(res.body.code==0){
  //                 this.showTip_8 = true;
  //                 this.tip_8 = '注册成功!'
  //
  //               }
  //               else{
  //                 this.showTip_8 = true;
  //                 this.tip_8 = res.body.object
  //               }
  //           },
  //
  //             (err)=>{
  //               this.showTip_8 = true;
  //               this.tip_8 ='网络错误，注册失败！'
  //             });
  //
  //           // this.$http.post(remoteLoc+"user/register",{
  //           //     'userPre':JSON.stringify(userPre)
  //           //   },
  //           //   {
  //           //     //emulateJSON: true,
  //           //     headers:{'Content-Type': 'application/json;charset=UTF-8'}
  //           //   }).then(function(res){
  //           //
  //           //   if(res.body.code==0){
  //           //     this.showTip_8 = true;
  //           //     this.tip_8 = '注册成功!'
  //           //
  //           //   }
  //           //   else{
  //           //     this.showTip_8 = true;
  //           //     this.tip_8 = res.body.object
  //           //   }
  //           // }, function(res){
  //           //   this.showTip_8 = true;
  //           //   this.tip_8 ='网络错误，注册失败！'
  //           // });
  //
  //
  //
  //
  //         }
  //
  //       }
  //
  //
  //
  //
  //
  //
  //
  //     },
  //     sendEmail(){
  //
  //       this.EmailCache = this.email
  //       adminAPI.isUsedEmail(this.email,
  //
  //         (response)=>{
  //           if(response.body.code==0&&response.body.object==false) {
  //             adminAPI.send(this.email, (res) => {
  //
  //               if (res.body.code == 0) {
  //                 this.showTip_8 = true;
  //                 this.tip_8 = '验证码已发送致' + this.EmailCache + '!'
  //                 this.ifGetCheck = 1;
  //               }
  //               else {
  //                 this.showTip_8 = true;
  //                 this.tip_8 = res.body.object
  //               }
  //             }, (err) => {
  //               this.showTip_8 = true;
  //               this.tip_8 = '网络错误，获取验证码失败！'
  //
  //
  //             })
  //           }
  //           else{
  //             this.showTip_8 = true;
  //             this.tip_8 = '该邮箱已被使用！'
  //           }
  //
  //
  //         },
  //
  //         (err)=>{
  //           this.showTip_8 = true;
  //           this.tip_8 ='网络错误，获取验证码失败！'
  //         }
  //
  //
  //
  //       )
  //       // this.$http.get(remoteLoc+"user/isUsedEmail",{
  //       //     'email' : this.email,
  //       //   },
  //       //   {emulateJSON: true}
  //       // ).then(function(response){
  //       //
  //       //   if(response.body.code==0&&response.body.object==false){
  //       //     this.$http.get(remoteLoc+"user/send",{'email' : this.email,}, {emulateJSON: true}
  //       //     ).then(function(res){
  //       //
  //       //       if(res.body.code==0){
  //       //         this.showTip_8 = true;
  //       //         this.tip_8 = '验证码已发送致'+this.EmailCache+'!'
  //       //         this.ifGetCheck=1;
  //       //       }
  //       //       else{
  //       //         this.showTip_8 = true;
  //       //         this.tip_8 = res.body.object
  //       //       }
  //       //     }, function(res){
  //       //       this.showTip_8 = true;
  //       //       this.tip_8 ='网络错误，获取验证码失败！'
  //       //     });
  //       //
  //       //   }
  //       //   else{
  //       //     this.showTip_8 = true;
  //       //     this.tip_8 = '该邮箱已被使用！'
  //       //   }
  //       //
  //       //
  //       // }, function(response){
  //       //   this.showTip_8 = true;
  //       //   this.tip_8 ='网络错误，获取验证码失败！'
  //       //
  //       // });
  //
  //
  //     },
  //
  //   }
  //
  //
  //
  // }

</script>


<style>
  /*.login-wrap{text-align:center;}*/
  /*input{display:block; width:250px; height:40px; line-height:40px; margin:0 auto; margin-bottom: 10px; outline:none; border:1px solid #888; padding:10px; box-sizing:border-box;}*/
  /*p{color:red;}*/
  /*button{display:block; width:250px; height:40px; line-height: 40px; margin:0 auto; border:none; background-color:#41b883; color:#fff; font-size:16px; margin-bottom:5px;}*/
  /*span{cursor:pointer;}*/
  /*span:hover{color:#41b883;}*/
  #reg {
    position: relative;
    left: 68%;
    top: 20%;
    width: 330px;
    /*width: 26%;*/
  }
  .registerBg {
    background-size: cover;
    background-image: url('../assets/background.jpg');
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    height: 100%;
  }
</style>



