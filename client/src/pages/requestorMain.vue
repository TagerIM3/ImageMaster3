<template>
  <div class="layout">
    <Layout>
      <Sider  :style="{position: 'absolute', height: '100%', left: 0, overflow: 'auto'}">
        <Menu active-name="3-1" theme="dark" width="auto" :open-names="['1']">
          <Submenu name="1">
            <template slot="title">
              <Icon type="ios-navigate"></Icon>
              我的任务
            </template>
            <MenuItem name="1-1" @click.native="show_postTask">发布任务</MenuItem>
            <MenuItem name="1-2" @click.native="show_showTask">查看任务</MenuItem>
          </Submenu>
          <Submenu name="3">
            <template slot="title">
              <Icon type="ios-analytics"></Icon>
              统计分析
            </template>
            <MenuItem name="3-1" @click.native="show_showInfo">统计信息</MenuItem>
            <!--<MenuItem name="3-2" >修改资料</MenuItem>-->
            <!--<MenuItem name="3-3" >退出登陆</MenuItem>-->
          </Submenu>
        </Menu>
      </Sider>

      <Layout :style="{marginLeft: '200px'}">
        <Header :style="headerStyle">
          <!--<div style="float:left">-->
            <!--<div  v-if="jump === 1">-->
              <!--<h3>发布任务</h3>-->
            <!--</div>-->
            <!--<div v-else-if="jump===0">-->
              <!--<h3>统计信息</h3>-->
            <!--</div>-->
            <!--<div v-else-if="jump===2">-->
              <!--<h3>查看任务</h3>-->
            <!--</div>-->
            <!--<div v-else-if="jump===3">-->
              <!--<h3>个人中心</h3>-->
            <!--</div>-->
            <!--<div v-else-if="jump===4">-->
              <!--<h3>消息中心</h3>-->
            <!--</div>-->
          <!--</div>-->
          <div style="float: right">
            <Dropdown>
              <Avatar icon="person" :src="user_avatar"/>
              <Icon type="arrow-down-b"></Icon>
              <DropdownMenu slot="list">
                <DropdownItem @click.native="show_updateInfo">个人中心</DropdownItem>
                <DropdownItem @click.native="logout">退出登录</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>
          <div style="float: right;padding-right: 30px" >
            <Tooltip content="系统消息" placement="bottom">
              <Badge dot :count="unread_message">
                <Icon type="ios-bell-outline" size="26" @click.native="show_messageCenter" color="#E6751E"></Icon>
              </Badge>
            </Tooltip>

          </div>

          <div style="float: right;padding-right: 30px;padding-top: 6px">
            <Tooltip content="交易记录" placement="bottom">
              <Icon type="social-yen" size="22" color="#E6751E" @click.native="show_tradeCenter"></Icon>
            </Tooltip>
          </div>

          <div style="float: left;padding-left: 30px">
            <p style="font-family: 华文琥珀;color:#0B9EF1;font-size: 42px">Tager</p>
          </div>
        </Header>
        <Content :style="{padding: '0 16px 16px'}">
          <div v-if="jump === 1">
            <postTask></postTask>
          </div>
          <div v-else-if="jump===0">
            <showInfo></showInfo>
          </div>
          <div v-else-if="jump===2">
            <showTask></showTask>
          </div>
          <div v-else-if="jump===3">
            <updateInfo></updateInfo>
          </div>
          <div v-else-if="jump===4">
            <messageCenter></messageCenter>
          </div>
          <div v-else-if="jump===6" style="padding-top: 3%">
            <trade-record></trade-record>
          </div>


        </Content>
        <div style="padding-top:50px;">
          <Footer class="layout-footer-center">Since 2018
            <img src="../assets/logo.png" style="height:20px;weight:20px;">
          </Footer>
        </div>
      </Layout>

    </Layout>

  </div>
</template>

<script>
  import showInfo from "../components/requestor/showInfo";
  import updateInfo from "../components/util/personalModify";
  import showTask from "../components/requestor/showTask";
  import postTask from "../components/requestor/postTask";
  import messageCenter from "../components/util/messageCenter";
  import TradeRecord from "../components/util/tradeRecord";
  import api from "../api/requestor"
  export default {
     components: {TradeRecord, messageCenter,showTask,postTask,showInfo,updateInfo },
    data () {
      return {
        jump: 2,
        unread_message:0,
        isCollapsed: false,
        user:{},
        user_avatar:"http://106.14.178.184:8080/im3",
        headerStyle: {
          boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)',
          height: '10vh',
          backgroundImage: "url(" + require("../assets/header3.jpg") + ")",
          backgroundRepeat: "no-repeat",
          backgroundSize: "100% 100%"


        }
      }
    },
    mounted(){
      this.user = this.$route.params.user;
      this.user_avatar = ""
      // console.log(this.user)
      api.getRequestor(
        (res)=>{
          console.log(res)
          if (res.message === 'SUCCESS') {
            this.user = res.object
            this.user_avatar = "http://106.14.178.184:8080/im3" + this.user.avatar
            if(!this.user.avatar)this.user_avatar = ""
          } else if (res.message === 'FAIL') {
            this.$Message.error('网络错误，请稍后再试')
          }
          else if(res.message === 'TIMEOUT'){
            this.$Message.error('您因为太久未操作而被登出！')
            this.$router.push({
              path: '/',
            });
          }
        },
        (err)=>{
          this.$Message.error(err.message);
        }
      )
    },
    computed: {
      rotateIcon () {
        return [
          'menu-icon',
          this.isCollapsed ? 'rotate-icon' : ''
        ];
      },
      menuitemClasses () {
        return [
          'menu-item',
          this.isCollapsed ? 'collapsed-menu' : ''
        ]
      }
    },
    methods: {
       show_tradeCenter(){
         this.jump = 6;
       },
      show_postTask(){
        // console.log(this.$route.params.user)
         this.jump = 1;
        // console.log(this.user)
       },

      show_showTask(){
        this.jump = 2;
        // console.log(this.user)

      },
      logout(){
        this.$router.push({path:'/'})

      },
      show_showInfo(){
        this.jump = 0;

      },
      show_messageCenter(){
        this.jump = 4;
      },
      show_updateInfo(){
        this.jump = 3;

      },

      collapsedSider () {
        this.$refs.side1.toggleCollapse();
      }
    }
  }
</script>

<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    min-height: 1000px;
  }
  .layout-header-bar{
    background: #fff;
    box-shadow: 0 1px 1px rgba(0,0,0,.1);
  }
  .layout-logo-left{
    width: 90%;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    margin: 15px auto;
  }
  .menu-icon{
    transition: all .3s;
  }
  .layout-footer-center{
    text-align: center;

    border-top:1px solid #fff
  }
  .menu-item span{

    display: inline-block;
    overflow: hidden;
    width: 69px;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: bottom;
    transition: width .2s ease .2s;
  }
  .menu-item i{
    transform: translateX(0px);
    transition: font-size .2s ease, transform .2s ease;
    vertical-align: middle;
    font-size: 16px;
  }
  .collapsed-menu span{
    width: 0px;
    transition: width .2s ease;
  }
  .collapsed-menu i{
    transform: translateX(5px);
    transition: font-size .2s ease .2s, transform .2s ease .2s;
    vertical-align: middle;
    font-size: 22px;
  }
</style>
