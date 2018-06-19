<template>
  <!--<div v-on:click="showTips">-->
  <!--<div class="login-wrap">-->
  <!--<h3>登录</h3>-->

  <!--<input type="text" placeholder="请输入用户名" v-model="username_email">-->
  <!--<p v-show="showTip_1">{{tip_1}}</p>-->
  <!--<input type="password" placeholder="请输入密码" v-model="password">-->
  <!--<p v-show="showTip_2">{{tip_2}}</p>-->
  <!--<button v-on:click="login">登录</button>-->
  <!--<p v-show="showTip_3">{{tip_3}}</p>-->
  <!--<span v-on:click="ToRegister">没有账号？马上注册</span>-->
  <!--</div>-->
  <!--</div>-->
  <div class="loginBg" style="min-width:1000px">
    <!--<img src="../assets/background.jpg" width="100%">-->
    <Button type="ghost" shape="circle" icon="person" id="toAdmin" @click="turnToAdmin"></Button>
    <!--<Icon type="person" id="toAdmin" size="large"></Icon>-->
    <Card id="card">
      <p slot="title">登录</p>
      <Form ref="formInline" :model="formInline" :rules="ruleInline" inline style="width:100%">
        <FormItem prop="username_email" style="width:100%">
          <i-input type="text" v-model="formInline.username_email" placeholder="用户名或邮箱" style="width: 100%">
            <Icon type="ios-person-outline" slot="prepend"></Icon>
          </i-input>
        </FormItem>
        <br>
        <FormItem prop="password" style="width:100%">
          <i-input type="password" v-model="formInline.password" placeholder="密码" style="width: 100%">
            <Icon type="ios-locked-outline" slot="prepend"></Icon>
          </i-input>
        </FormItem>
        <br>

        <Button type="primary" @click="handleSubmit('formInline')" style="width: 100%">登录</Button>
        <br><br>
        <div style="width: 95%">
          <a @click="ToRegister">没有账号？马上注册</a>
          <a @click="ToForgetPassword" style="float:right">忘记密码</a>
        </div>


      </Form>
    </Card>
  </div>
</template>


<script>
  import remoteLoc from '../util/remote.js'
  import userAPI from '../api/user'
  import Img from '../assets/background.jpg'

  export default {
    data() {
      return {
        img: Img,
        ifCanLogin: 1,
        ifGetCheck: 1,
        ifJump: 0,
        formInline: {
          username_email: '',
          password: ''
        },
        ruleInline: {
          username_email: [
            {required: true, message: '用户名不能为空', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '密码不能为空', trigger: 'blur'},
            //{type: 'string', min: 6, message: '密码长度不得少于6位', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      turnToAdmin() {
        this.$router.push('/adminLogin');
      },
      handleSubmit(name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            // this.$Message.success('Success!');
            this.login();
          } else {
            this.$Message.error('请输入正确的登录信息');
          }
        })
      },
      login() {

        userAPI.login(this.formInline.username_email, this.formInline.password,
          (res) => {
            var message = res.message;
            if (message === 'SUCCESS') {
              this.$Message.success('登录成功');

              var user = res.object;
              var typeOfUser = user.userType;
              if (typeOfUser === 'REQUESTOR') {
                // console.log(user)
                this.$router.push({
                  name: 'requestorMain',
                  params:{user: user}

                });
              } else if (typeOfUser === 'WORKER') {
                this.$router.push({
                  name: 'workerMain',
                  params:{user: user}

                });
              }
            } else if (message === 'WRONG') {
              this.$Message.error('登录失败，请检查您的输入信息是否正确')
            } else if (message === 'FAIL') {
              this.$Message.error('登录失败')
            }
          },
          (err) => {
            this.$Message.error(err.message);
          }
        )

      },
      ToRegister() {
        this.$router.push('/Register');
      },
      ToForgetPassword() {
        this.$router.push('/forgetPassword');
      }
    }
  }
  // export default{
  //   data(){
  //
  //
  //
  //     return{
  //       showLogin: true,
  //       showRegister: false,
  //       showTip_1: false,
  //       showTip_2: false,
  //       showTip_3: false,
  //
  //       tip_1: '',tip_2: '',tip_3: '',
  //       username_email: '',
  //       password: '',
  //
  //       ifCanLogin:1,
  //       ifCanRegist:1,
  //       ifGetCheck:1,
  //       ifJump:0,
  //     }
  //   },
  //
  //   methods:{
  //     showTips(){
  //       this.showTip_1 = false;
  //       this.showTip_2 = false;
  //
  //
  //       if(this.ifJump){
  //
  //         this.ifJump=0
  //         this.showTip_1 = false;
  //         this.showTip_2 = false;
  //         this.showTip_3 = false;
  //         return
  //       }
  //       this.ifCanLogin=1;this.ifCanRegist=1;
  //       //登陆界面tips
  //       if(this.username_email==""){
  //         this.showTip_1=true
  //         this.tip_1="用户名不能为空"
  //         this.ifCanLogin=0
  //       }
  //
  //
  //       if(this.password==""){
  //         this.showTip_2=true
  //         this.tip_2="密码不能为空"
  //         this.ifCanLogin=0;
  //       }
  //
  //     },
  //
  //
  //     ToRegister(){
  //       this.showTip_1 = false;
  //       this.showTip_2 = false;
  //       this.showTip_3 = false;
  //
  //       this.showLogin = false
  //       this.showRegister = true
  //       this.username_email = ""
  //       this.password = ""
  //       this.ifJump=1;
  //       this.$router.push({path:'/Register'})
  //     },
  //
  //     login(){
  //       this.$router.push({path:'/worker/workerMain'});
  //       if(this.ifCanLogin){
  //
  //         adminAPI.login(this.username_email,this.password,
  //
  //           (response)=>{
  //
  //             if(response.body.code==-1){
  //               this.showTip_3 = true;
  //               this.tip_3 =response.body.object
  //             }
  //             else{
  //               this.showTip_3 = true;
  //               this.tip_3 ='登陆成功'
  //             }
  //         },
  //
  //         (err)=>{
  //
  //           this.showTip_3 = true;
  //           this.tip_3 ='网络错误'
  //         }
  //
  //         )
  //
  //       }
  //       else{
  //         this.showTip_3 = true;
  //         this.tip_3 = '请输入正确的登陆信息!'
  //       }
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
  #card {
    position: relative;
    left: 68%;
    top: 20%;
    width: 330px;
    /*width: 26%;*/
    /*height: 45%*/
  }

  #toAdmin {
    position: relative;
    left: 1250px;
    top: 20px;

  }

  .loginBg {

    background-image: url('../assets/background.jpg');
    background-repeat: no-repeat;
    background-position: center center;
    background-attachment: fixed;
    background-size: cover;
    width: 100%;
    height: 100%;
  }

  /*.login-wrap{text-align:center;}*/
  /*input{display:block; width:250px; height:40px; line-height:40px; margin:0 auto; margin-bottom: 10px; outline:none; border:1px solid #888; padding:10px; box-sizing:border-box;}*/
  /*p{color:red;}*/
  /*button{display:block; width:250px; height:40px; line-height: 40px; margin:0 auto; border:none; background-color:#41b883; color:#fff; font-size:16px; margin-bottom:5px;}*/
  /*span{cursor:pointer;}*/
  /*span:hover{color:#41b883;}*/
</style>


