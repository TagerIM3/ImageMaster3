<template>
  <div id="d">
    <i-input placeholder="请输入管理员密钥" id="key" v-model="value" type="password"></i-input>
    <Button type="primary" id="butt" @click="admLog">确定</Button>
  </div>

</template>

<script>
  import adminAPI from '../api/administrator'

  export default {
    name: "adminLogin",
    data() {
      return {
        value: ''
      }

    },
    methods: {
      admLog() {
        // this.$Message.success(this.value);
        // var key = 'tager';
        adminAPI.login(this.value, (res) => {
            if (res === 'SUCCESS') {
              this.$router.push('/administrator');
            }
            else {
              this.$Message.error('密钥错误');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          }
        )
      }
    }
  }
</script>

<style scoped>
  #key {
    position: relative;
    left: 35%;
    top: 200px;
    width: 300px;
  }

  #butt {
    position: relative;
    left: 35%;
    top: 200px;

  }

  #d {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: black;
  }

</style>
