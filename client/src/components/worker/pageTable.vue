<template>
  <div>
    <i-input style="width:250px" icon="search" placeholder="输入关键字搜索" v-model="searchWord" @on-change="searchTask"></i-input>
    <Button type="primary" @click="searchTask">搜一下</Button>
    <!--<Select v-model="selectRecommend" size="large" style="width:100px">-->
    <!--<Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>-->
    <!--</Select>-->
    <!--<Button type="primary" style="position:relative;left:50%" @click="recommend">我的推荐</Button>-->
    <br><br>

    <Menu mode="horizontal" :theme="theme1" active-name="1" v-model="selectRecommend" @on-select="showTasks">
      <MenuItem name="1">
        <Icon type="ios-paper"></Icon>
        我的推荐
      </MenuItem>
      <MenuItem name="2">
        <Icon type="ios-people"></Icon>
        擅长
      </MenuItem>
      <MenuItem name="3">
        <Icon type="settings"></Icon>
        按奖励排序
      </MenuItem>
    </Menu>

    <Table :loading="loading" :data="show_data" :columns="tableColumns1" stripe></Table>
    <div style="margin: 10px;overflow: hidden">
      <div style="float: right;">
        <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
      </div>
    </div>

    <Modal
      v-model="viewDescription"
      title="任务描述"
      @on-ok="chooseTask"
      @on-cancel="">
      <p>{{descMessage}}</p><br>
      <p>确定选择此任务？</p>
    </Modal>
  </div>
</template>
<script>
  import workerAPI from '../../api/worker'

  export default {
    data() {
      return {
        // tableData1: this.mockTableData1(),
        searchWord: '',
        descMessage: '',
        viewDescription: false,
        nameType: 1,
        loading: true,
        data: [], //总的任务，并且push了'imageNum'属性进去
        data_length: 0,
        search_show_data: [],
        show_data: [],
        selectRecommend: '',
        theme1: 'light',
        tableData1: [],
        // idList: [],
        // nameList: [],
        // descriptionList: [],
        // bonusList: [],
        // ddl: [],
        indexOfPage: 1,
        indexOfTask: 0,
        typeList: [
          {
            value: ''
          }
        ],
        tableColumns1: [
          {
            title: '任务名',
            key: 'taskName',
          },
          {
            title: '任务类型',
            key: 'label',
          },
          {
            title: '图片数量',
            key: 'imageNum'
          },
          {
            title: '积分奖励',
            key: 'reward',
            // render: (h, params) => {
            //   return h('Poptip', {
            //     props: {
            //       trigger: 'hover',
            //       title: params.row.people.length + 'customers',
            //       placement: 'bottom'
            //     }
            //   }, [
            //     h('Tag', params.row.people.length),
            //     h('div', {
            //       slot: 'content'
            //     }, [
            //       h('ul', this.tableData1[params.index].people.map(item => {
            //         return h('li', {
            //           style: {
            //             textAlign: 'center',
            //             padding: '4px'
            //           }
            //         }, item.n + '：' + item.c + 'People')
            //       }))
            //     ])
            //   ]);
            // }
          },
          {
            title: '任务操作',
            key: 'action',
            // width: 150,
            align: 'center',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    // size: 'small'
                  },
                  // style: {
                  //   marginRight: '5px'
                  // },
                  on: {
                    click: () => {
                      this.viewTask(params.index);
                    }
                  }
                }, '查看'),
                // h('Button', {
                //   props: {
                //     type: 'error',
                //     size: 'small'
                //   },
                //   on: {
                //     click: () => {
                //       this.remove(params.index)
                //     }
                //   }
                // }, 'Delete')
              ]);
            }
          }
        ]
      }
    },
    mounted() {
      this.showTasks();
    },
    methods: {
      searchTask(){
        // this.$Message.success(this.searchWord);
        this.search_show_data = [];
        for(let i=0;i<this.data.length;i++){
          if(this.data[i].description.indexOf(this.searchWord)>-1||this.data[i].label.indexOf(this.searchWord)>-1){
            // let isContained = false;
            // for(let j =0;j<this.search_show_data.length;j++){
            //   if(this.search_show_data[j].id === this.data[i].id){
            //     isContained = true;
            //   }
            // }

              this.search_show_data[this.search_show_data.length]=this.data[i];


          }
        }
        console.log(this.search_show_data);
        console.log(this.searchWord);
        this.indexOfPage = 1;
        this.show_data=[];
        let loc = 5;
        let i;
        if (loc > this.search_show_data.length) {
          for (i = 0; i < this.search_show_data.length - loc + 5; i++) {
            this.show_data[i] = this.search_show_data[loc - 5 + i];
          }
        }
        else {
          for (i = 0; i < 5; i++) {
            this.show_data[i] = this.search_show_data[loc - 5 + i];
          }
        }
      },
      viewTask(index) {
        this.descMessage = this.show_data[index].description;
        this.viewDescription = true;
        this.indexOfTask = index;
      },
      showTasks(name) {
        this.nameType = name;
        if (name === '1') {
          workerAPI.getRecommendTasks((res) => {
              // console.log(this.$router.path);
              //   console.log(res);
              if (res.message === 'SUCCESS') {
                // console.log(res.object);
                this.data = res.object;
                this.data_length = this.data.length;
                this.loading = false;
                for (let i = 0; i < res.object.length; i++) {
                  this.data[i]['imageNum'] = this.data[i].images.length;
                }
                this.changePage(this.indexOfPage);
                console.log(this.show_data);
                // for (var i = 0; i < 5 * this.indexOfPage; i++) {
                //   var item = works[i];
                //   this.idList[i] = item.id;
                //   this.nameList[i] = item.taskName;
                //   this.descriptionList[i] = item.description;
                //   this.bonusList[i] = item.reward;
                //   this.ddl[i] = item.finishTime;
                // }
              } else if (res.message === 'TIMEOUT') {
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              } else {
                this.$Message.error('网络错误，请稍后再试');
              }
            },
            (err) => {
              // console.log("err");
              this.$Message.error(err.message);
            });
        }
        else if (name === '2') {
          workerAPI.getSkilledTasks((res) => {
              // console.log(res);
              if (res.message === 'SUCCESS') {
                this.data = res.object;
                this.data_length = this.data.length;
                this.loading = false;
                for (let i = 0; i < res.object.length; i++) {
                  this.data[i]['imageNum'] = this.data[i].images.length;
                }
                this.changePage(this.indexOfPage);
              } else if (res.message === 'TIMEOUT') {
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              } else {
                this.$Message.error('网络错误，请稍后再试');
              }
            },
            (err) => {
              this.$Message.error(err.message);
            });
        }
        else {
          workerAPI.getRewardTasks((res) => {
              console.log(res);
              if (res.message === 'SUCCESS') {
                this.data = res.object;
                this.data_length = this.data.length;
                this.loading = false;
                for (let i = 0; i < res.object.length; i++) {
                  this.data[i]['imageNum'] = this.data[i].images.length;
                }
                this.changePage(this.indexOfPage);
              } else if (res.message === 'TIMEOUT') {
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              } else {
                this.$Message.error('网络错误，请稍后再试');
              }
            },
            (err) => {
              this.$Message.error(err.message);
            });
        }

      },


      changePage(pageSize) {
        this.indexOfPage = pageSize;
        this.show_data=[];
        let loc = pageSize * 5;
        let i;
        if (loc > this.data.length) {
          for (i = 0; i < this.data.length - loc + 5; i++) {
            this.show_data[i] = this.data[loc - 5 + i];
          }
        }
        else {
          for (i = 0; i < 5; i++) {
            this.show_data[i] = this.data[loc - 5 + i];
          }
        }


      },

      chooseTask() {
        workerAPI.acceptTask(this.show_data[this.indexOfTask].id, (res) => {
            if (res === 'SUCCESS') {
              //刷新界面
              this.showTasks(this.nameType);
            }
            else if (res === 'TIMEOUT') {
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
      }
    }
  }
</script>
