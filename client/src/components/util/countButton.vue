<!--倒计时功能的Button-->

<template>
  <Button @click="countDown" type="primary" class="Button" :class="{disabled: !this.canClick}">{{content}}</Button>
</template>

<script>
  import remoteLoc from '../../util/remote.js'
  import userAPI from '../../api/user'
  export default {
    name: "countButton",
    data() {
      return {
        content: '获取验证码',
        totalTime: 30,
        canClick: true //添加canClick
      }
    },
    methods: {
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
            this.content = '重新发送验证码';
            this.totalTime = 30;
            this.canClick = true  //这里重新开启
          }
        }, 1000)
      },
      sendEmail() {

        this.formInline.EmailCache = this.formInline.email;
        alert(this.formInline.EmailCache);
        userAPI.isUsedEmail(this.formInline.email,

          (response) => {
            if (response.body.code === 0 && response.body.object === false) {
              userAPI.send(this.formInline.email, (res) => {

                if (res.body.code === 0) {
                  // this.showTip_8 = true;
                  // this.tip_8 = '验证码已发送致' + this.EmailCache + '!'
                  this.$Message.success('验证码已发送致' + this.formInline.EmailCache);
                }
                else {
                  // this.showTip_8 = true;
                  // this.tip_8 = res.body.object
                  this.$Message.error(res.body.object);
                }
              }, (err) => {
                // this.showTip_8 = true;
                // this.tip_8 = '网络错误，获取验证码失败！'
                this.$Message.error('网络错误，获取验证码失败');
              })
            }
            else {
              // this.showTip_8 = true;
              // this.tip_8 = '该邮箱已被使用！'
              this.$Message.error('该邮箱已被使用');
            }


          },

          (err) => {
            // this.showTip_8 = true;
            // this.tip_8 = '网络错误，获取验证码失败！'
            this.$Message.error('该邮箱已被使用');
          }
        )
      }

    }

  }
</script>

<style scoped>
  .disabled{
    background-color: #ddd;
    border-color: #ddd;
    color:#57a3f3;
    cursor: not-allowed;

  }
</style>
