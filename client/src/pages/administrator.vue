<template>
  <div class="layout">
    <Layout>
      <Sider :style="{position: 'absolute', height: '100%', left: 0, overflow: 'auto'}">
        <Menu active-name="1-1" theme="dark" width="auto" :open-names="['1']">
          <Submenu name="1">
            <template slot="title">
              <Icon type="ios-navigate"></Icon>
              统计分析
            </template>
            <MenuItem name="1-1" @click.native="show_newTask">统计信息</MenuItem>
          </Submenu>
        </Menu>
      </Sider>
    </Layout>

    <Layout :style="{marginLeft: '200px'}">
      <Header :style="headerStyle">
        <div style="float: left;padding-left: 30px">
          <p style="font-family: 华文琥珀;color:#0B9EF1;font-size: 42px">Tager</p>
        </div>
        <a @click="logout" style="position:relative;left:80%;font-size: medium">返回首页</a>
      </Header>
      <Content :style="{padding: '0 16px 16px'}">
        <div style="padding-top: 3%">

          <div style="height: 250px;width:48%;float:left">
            <Card>
              <p slot="title">用户统计</p>
              <div id="card1" style="height: 160px;width:90%;left:5%" v-if="showCard1===0">

              </div>
              <div id="card1-1" style="height: 160px;width:90%;left:5%" v-if="showCard1===1">
                <Alert type="error" show-icon>加载失败，暂无数据</Alert>
              </div>
            </Card>
          </div>

          <div style="height: 250px;width:52%;float:left;padding-left: 4%">
            <Card>
              <p slot="title">任务统计</p>
              <div id="card2" style="height: 160px;width:90%;left:5%" v-if="showCard2===0">

              </div>
              <div id="card2-1" style="height: 160px;width:90%;left:5%" v-if="showCard2===1">
                <Alert type="error" show-icon>加载失败，暂无数据</Alert>
              </div>
            </Card>
          </div>
          <div style="padding-top: 25%">
            <create-users-in-seven></create-users-in-seven>
            <create-users-in-thirty></create-users-in-thirty>
            <finish-tasks-in-seven></finish-tasks-in-seven>
            <finish-tasks-in-thirty></finish-tasks-in-thirty>
            <reward_time></reward_time>
            <!--<image_time></image_time>-->
          </div>

          <!--<submit-tasks-in-seven></submit-tasks-in-seven>-->
          <!--<submit-tasks-in-thirty></submit-tasks-in-thirty>-->

        </div>

      </Content>
      <div style="padding-top:50px;">
        <Footer class="layout-footer-center">Since 2018
          <img src="../assets/logo.png" style="height:20px;weight:20px;">
        </Footer>
      </div>
    </Layout>

  </div>
</template>

<script>
  import createUsersInSeven from '../components/administrator/createUsersInSeven'
  import createUsersInThirty from '../components/administrator/createUsersInThirty'
  import finishTasksInSeven from '../components/administrator/finishTasksInSeven'
  import finishTasksInThirty from '../components/administrator/finishTasksInThirty'
  import submitTasksInSeven from '../components/administrator/submitTasksInSeven'
  import submitTasksInThirty from '../components/administrator/submitTasksInThirty'
  import reward_time from '../components/administrator/reward_time'
  import image_time from '../components/administrator/image_time'
  import adminAPI from '../api/administrator'

  export default {
    components: {
      createUsersInSeven,
      createUsersInThirty,
      submitTasksInSeven,
      submitTasksInThirty,
      finishTasksInSeven,
      finishTasksInThirty,
      reward_time,
      image_time,
    },
    name: "administrator",
    data() {
      return {
        workersNum: 0,
        requestorsNum: 0,
        finishTasks: 0,
        unfinishTasks: 0,
        showCard1: 0,
        showCard2: 0,
        headerStyle: {
          boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)',
          height: '10vh',
          backgroundImage: "url(" + require("../assets/header3.jpg") + ")",
          backgroundRepeat: "no-repeat",
          backgroundSize: "100% 100%"


        },
      }
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
    mounted() {
      this.showData();
      // this.drawCard1();
      // this.drawCard2();
      // this.drawCard3();
      // this.drawCard();
    },
    methods: {
      logout() {
        this.$router.push('/');
      },
      showData() {
        //alert("ok")
        adminAPI.getStatistics((res) => {
            if (res.message === 'SUCCESS') {
              var adminSta = res.object;
              var users = adminSta.users;
              var tasks = adminSta.tasks;
              this.workersNum = users[0];
              this.requestorsNum = users[1];
              this.finishTasks = tasks[0];
              this.unfinishTasks = tasks[1];
              this.drawCard1();
              this.drawCard2();
            } else {
              this.showCard1=1;
              this.showCard2=1;
            }
          },
          (err) => {
            this.showCard1=1;
            this.showCard2=1;
            this.$Message.error(err);
          })
      },
      drawCard1() {
        let myChart1 = this.$echarts.init(document.getElementById('card1'));
        myChart1.setOption({
          tooltip: {
            // title: { text: '用户统计'},
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
          },
          legend: {
            orient: 'vertical',
            x: 'left',
            data: ['工人', '发起者']
          },
          series: [
            {
              name: '比例',
              type: 'pie',
              radius: ['50%', '70%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: false,
                  position: 'left'
                },
                emphasis: {
                  show: true,
                  textStyle: {
                    fontSize: '15',
                    fontWeight: 'bold'
                  }
                }
              },
              labelLine: {
                normal: {
                  show: false
                }
              },
              data: [
                {value: this.workersNum, name: '工人'},
                {value: this.requestorsNum, name: '发起者'},
              ]
            }
          ]
        });

      },

      drawCard2() {
        let myChart2 = this.$echarts.init(document.getElementById('card2'));
        myChart2.setOption(
          {
            tooltip: {
              // title: { text: '用户统计'},
              trigger: 'item',
              formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
              orient: 'vertical',
              x: 'left',
              data: ['已完成任务数', '未完成任务数']
            },
            series: [
              {
                name: '比例',
                type: 'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                  normal: {
                    show: false,
                    position: 'left'
                  },
                  emphasis: {
                    show: true,
                    textStyle: {
                      fontSize: '15',
                      fontWeight: 'bold'
                    }
                  }
                },
                labelLine: {
                  normal: {
                    show: false
                  }
                },
                data: [
                  {value: this.finishTasks, name: '已完成任务数'},
                  {value: this.unfinishTasks, name: '未完成任务数'},
                ]
              }
            ]
          }
        );
      },


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
  .layout-footer-center{
    text-align: center;

    border-top:1px solid #fff
  }
</style>
