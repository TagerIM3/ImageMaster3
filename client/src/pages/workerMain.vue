<template>
  <div class="layout">
    <Layout>
      <Sider :style="{position: 'absolute', height: '100%', left: 0, overflow: 'auto',}">
        <Menu active-name="1-1" theme="dark" width="auto" :open-names="[]">
          <Submenu name="1">
            <template slot="title">
              <Icon type="ios-navigate"></Icon>
              我的任务
            </template>
            <MenuItem name="1-1" @click.native="show_newTask">新任务</MenuItem>
            <MenuItem name="1-2" @click.native="show_showTask">已选择任务</MenuItem>
            <MenuItem name="1-3" @click.native="show_finishTask">已提交任务</MenuItem>
          </Submenu>

          <Submenu name="2">
            <template slot="title">
              <Icon type="ios-analytics"></Icon>
              统计分析
            </template>
            <MenuItem name="2-1" @click.native="show_statistics">统计信息</MenuItem>
          </Submenu>

          <!--<Submenu name="3">-->
            <!--<template slot="title">-->
              <!--<Icon type="ios-analytics"></Icon>-->
              <!--个人设置-->
            <!--</template>-->
            <!--&lt;!&ndash;<MenuItem name="3-1" @click.native="show_showInfo">查看资料</MenuItem>&ndash;&gt;-->
            <!--<MenuItem name="3-2" @click.native="show_updateInfo">修改资料</MenuItem>-->
            <!--<MenuItem name="3-3" @click.native="logout">退出登录</MenuItem>-->
          <!--</Submenu>-->
        </Menu>
        <!--<Menu theme="light" width="auto">-->
        <!--<p>hello</p>-->
        <!--</Menu>-->
      </Sider>


      <Layout :style="{marginLeft: '200px'}">
        <!--<Header :style="{background: '#fff', boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)',height:'10vh'}">-->
        <Header
          :style="headerStyle">
          <!--<div>-->
          <!--<img src="../../assets/logo.png" />-->
          <!--</div>-->
          <div style="float: right">
            <Dropdown>
              <Avatar icon="person" :src="user_avatar"/>
              <Icon type="arrow-down-b"></Icon>
              <DropdownMenu slot="list">
                <DropdownItem @click.native="show_setup">个人中心</DropdownItem>
                <!--<DropdownItem @click.native="show_trade">交易记录</DropdownItem>-->
                <DropdownItem @click.native="logout">退出登录</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </div>

          <div style="float: right;padding-right: 30px">
            <Tooltip content="系统消息" placement="bottom">
              <Badge dot :count="unread_message">
                <Icon type="ios-bell-outline" size="26" @click.native="show_messageCenter" color="#E6751E"></Icon>
              </Badge>
            </Tooltip>

          </div>

          <div style="float: right;padding-right: 30px;padding-top: 6px">
            <Tooltip content="交易记录" placement="bottom">
              <Icon type="social-yen" size="22" color="#E6751E" @click.native="show_trade"></Icon>
            </Tooltip>
          </div>

          <div style="float: left;padding-left: 30px">
            <p style="font-family: 华文琥珀;color:#0B9EF1;font-size: 42px">Tager</p>
          </div>

        </Header>
        <Content :style="{padding: '0 16px 16px'}">
          <!--<div style="padding-top: 2%" v-if="showPage===4">-->
          <!--<Card style="width:20%;height: 100px;float: left;left:4%"></Card>-->
          <!--<Card style="width:20%;height: 100px;float: left;left:8%"></Card>-->
          <!--<Card style="width:20%;height: 100px;float: left;left:12%"></Card>-->
          <!--<Card style="width:20%;height: 100px;float: left;left:16%"></Card>-->
          <!--</div>-->

          <div style="padding-top: 3%;height:100%;float: left" v-if="showPage===0">
            <Card style="width:100%;height:100%">
              <p slot="title">选择新任务</p>
              <page-table></page-table>
            </Card>
          </div>

          <div v-else-if="showPage===8">
            <finish-tasks></finish-tasks>
          </div>

          <div v-else-if="showPage===3">
            <personal-modify></personal-modify>
          </div>

          <div v-else-if="showPage===4">
            <worker-statistics></worker-statistics>
          </div>

          <!--<div v-else-if="showPage===6">-->
          <!--<worker-statistics></worker-statistics>-->
          <!--</div>-->

          <div v-else-if="showPage===2">
            <message-center></message-center>
          </div>

          <div v-else-if="showPage===7" style="padding-top: 3%;width:100%">
            <trade-record></trade-record>
          </div>


          <div style="padding-top: 3%;height:100%;float:left" v-else-if="showPage===5">

              <chosen-task></chosen-task>

          </div>

        </Content>
        <div style="padding-top:50px;">
          <Footer class="layout-footer-center">Since 2018
            <img src="../assets/logo.png" style="height:20px;weight:20px;">
          </Footer>
        </div>
        <!--<Footer :style="{background: '#494653', boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)',height:'10vh',}">-->

        <!--</Footer>-->

      </Layout>

    </Layout>
  </div>
</template>

<script>
  import uploadFile from '../components/util/uploadFile';
  import newTask from '../components/worker/newTask';
  import pageTable from '../components/worker/pageTable'

  // import updateInfo from '../requestor/updateInfo'
  import workerStatistics from '../components/worker/workerStatistics'
  import chosenTask from '../components/worker/chosenTask'
  import messageCenter from '../components/util/messageCenter'
  import personalModify from '../components/util/personalModify'
  import tradeRecord from '../components/util/tradeRecord'
  import finishTasks from '../components/worker/finishTasks'
  import api from "../api/worker"

  export default {
    components: {
      uploadFile,
      newTask,
      pageTable,
      workerStatistics,
      chosenTask,
      messageCenter,
      personalModify,
      tradeRecord,
      finishTasks
    },
    data() {
      return {
        isCollapsed: false,
        ifNewTask: false,
        showPage: 0,
        unread_message: 0,
        carousel: 0,
        headerStyle: {
          boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)',
          height: '10vh',
          backgroundImage: "url(" + require("../assets/header3.jpg") + ")",
          backgroundRepeat: "no-repeat",
          backgroundSize: "100% 100%"


        },
        user:{},
        user_avatar:"http://106.14.178.184:8080/im3",
      }
    },
    mounted(){
      this.user = this.$route.params.user;
      this.user_avatar = ""
      // console.log(this.user)
      api.getWorker(
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
      rotateIcon() {
        return [
          'menu-icon',
          this.isCollapsed ? 'rotate-icon' : ''
        ];
      },
      menuitemClasses() {
        return [
          'menu-item',
          this.isCollapsed ? 'collapsed-menu' : ''
        ]
      }
    },
    methods: {
      collapsedSider() {
        this.$refs.side1.toggleCollapse();
      },
      showNewTask() {
        this.ifNewTask = true;
      },
      logout() {
        this.$router.push('/');
      },
      show_updateInfo() {
        this.showPage = 3;
      },
      show_newTask() {
        this.showPage = 0;
      },
      show_showTask() {
        this.showPage = 5;
      },
      show_statistics() {
        this.showPage = 4;
      },
      show_messageCenter() {
        this.showPage = 2;
      },
      show_setup() {
        this.showPage = 3;
      },
      show_trade() {
        this.showPage = 7;
      },
      show_finishTask() {
        this.showPage = 8
      }
    }
  }
</script>

<style scoped>
  .layout {
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
    min-height: 1000px;
  }

  .layout-header-bar {
    background: #fff;
    box-shadow: 0 1px 1px rgba(0, 0, 0, .1);
  }

  .layout-logo-left {
    width: 90%;
    height: 30px;
    background: #5b6270;
    border-radius: 3px;
    margin: 15px auto;
  }

  .menu-icon {
    transition: all .3s;
  }

  .rotate-icon {
    transform: rotate(-90deg);
  }

  .layout-footer-center{
    text-align: center;

    border-top:1px solid #fff
  }
  .menu-item span {

    display: inline-block;
    overflow: hidden;
    width: 69px;
    text-overflow: ellipsis;
    white-space: nowrap;
    vertical-align: bottom;
    transition: width .2s ease .2s;
  }

  .menu-item i {
    transform: translateX(0px);
    transition: font-size .2s ease, transform .2s ease;
    vertical-align: middle;
    font-size: 16px;
  }

  .collapsed-menu span {
    width: 0px;
    transition: width .2s ease;
  }

  .collapsed-menu i {
    transform: translateX(5px);
    transition: font-size .2s ease .2s, transform .2s ease .2s;
    vertical-align: middle;
    font-size: 22px;
  }
</style>
