<template>
  <div style="padding-top: 3%;width:100%">

    <!--<Breadcrumb :style="{margin: '20px 0'}">-->
    <!--<BreadcrumbItem></BreadcrumbItem>-->
    <!--</Breadcrumb>-->

    <div style="min-height: 200px;padding-left: 0px;width:100%">

      <Card style="width:100%;">
        <p slot="title">
          <Icon type="ios-film-outline"></Icon>
          系统消息
        </p>

        <!--<div>-->
        <!--<Table :loading="loading" :columns="columns1" :data="show_data"></Table>-->

        <!--</div>-->
        <!--<div style="margin: 10px;overflow: hidden">-->
        <!--<div style="float: right;">-->
        <!--<Page :total="data_length" :current="page_loc" @on-change="changePage" simple></Page>-->
        <!--</div>-->
        <!--</div>-->

        <Menu mode="horizontal" :theme="theme1" active-name="1" @on-select="showMessage">
          <MenuItem name="1">
            <Icon type="ios-paper"></Icon>
            所有消息
          </MenuItem>
          <MenuItem name="2">
            <Icon type="ios-people"></Icon>
            已读消息
          </MenuItem>
          <MenuItem name="3">
            <Icon type="settings"></Icon>
            未读消息
          </MenuItem>
        </Menu>

        <Table :loading="loading" :data="show_data" :columns="columns2" stripe ref="selection"
               @on-row-click="rowClick"></Table>
        <div style="float: right;padding-top: 3%">
          <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
        </div>
        <div style="padding-top: 3%">
          <Button @click="deleteAllMessage" :disabled="delButton">全部删除</Button>
          <Button @click="readAllMessage" :disabled="readButton">全部已读</Button>
        </div>
      </Card>

      <Modal
        v-model="messageRead"
        title="消息详情"
        @on-ok="">
        <p>{{messageReading}}</p>

      </Modal>
    </div>


  </div>
</template>

<script>
  import api from '../../api/requestor'
  import workerAPI from '../../api/worker'

  export default {
    name: "messageCenter",
    data() {
      return {
        currentName: '1',
        messageReading: '',
        messageRead: false,
        delButton: false,
        readButton: false,
        theme1: 'dark',
        MessageList: [],
        loading: true,
        page_loc: 1,
        unread_message: [],
        readed_message: [],
        data1: [],
        to_show_data: [],
        data_length: 0,
        show_data: [],
        columns2: [
          {
            title: '内容',
            key: 'content'
          },
          {
            title: '时间',
            key: 'time',
            width: 220
          }
        ],

        columns1: [

          {
            title: 'ID',
            key: 'id'
          },
          {
            title: '创建时间',
            key: 'time'
          },
          {
            title: '内容',
            key: 'content'
          },
          {
            title: "删除",
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    // size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      // this.show()
                      let data = {}
                      data['messageId'] = this.show_data[params.index].id;
                      api.deleteMessages(data,
                        (res) => {
                          //console.log(res)
                          if (res === 'SUCCESS') {
                            this.$Message.success("删除成功")
                            this.loading = true;
                            this.getMessage()
                            this.page_loc = 1;
                          } else if (res === 'WRONG') {
                            this.$Message.error('网络错误，请稍后再试')
                          } else if (res === 'FAIL') {
                            this.$Message.error('网络错误，请稍后再试')
                          }
                          else if (res === 'TIMEOUT') {
                            this.$Message.error('您因为太久未操作而被登出！')
                            this.$router.push({
                              path: '/',
                            });
                          }
                        },
                        (err) => {
                          this.$Message.error(err.message);
                        }
                      )
                    }
                  }
                }, '删除'),

              ]);
            }
          }
        ],

      }
    },
    mounted() {
      this.show_data = [];
      this.showMessage('1');
    },
    methods: {
      // rowChange(currentRow,oldRow){
      //   // this.$Message.success(currentRow);
      //   // this.$Message.success(oldRow);
      // },
      rowClick(data) {

        workerAPI.readMessage(data['id'], (res) => {
            // console.log(res)
            if (res === 'SUCCESS') {
              this.messageReading = data['content'];
              this.messageRead = true;
              this.showMessage(this.currentName);
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

        // console.log(data['id']);
      },
      deleteAllMessage() {
        // console.log(this.$data.show_data[i])
        let delNum = 0;
        for (let i = 0; i < this.show_data.length; i++) {
          if (this.show_data[i].ifRead) {
          workerAPI.deleteMessage(this.show_data[i].id, (res) => {
              if (res === 'SUCCESS') {
                // this.showMessage('1');
                // this.$Message.success('消息列表已刷新');
                delNum = delNum + 1;
              } else if (res === 'TIMEOUT') {
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              } else {
                this.$Message.error('网络错误，请稍后再试')
              }
            },
            (err) => {
              this.$Message.error(err.message);
            });
          }
        }

          this.showMessage(this.currentName);
          this.$Message.success('消息列表已刷新');



      },
      readAllMessage() {
        for (let i = 0; i < this.show_data.length; i++) {
          // console.log(this.$data.show_data[i]._checked)
          if (!this.show_data[i].ifRead) {
          let delNum =0;
            workerAPI.readMessage(this.show_data[i].id, (res) => {
                // console.log(res);
                if (res === 'SUCCESS') {
                  delNum = delNum +1;
                  // this.showMessage('1');
                  // this.$Message.success('消息列表已刷新');
                } else if (res === 'TIMEOUT') {
                  this.$Message.error('您因为太久未操作而被登出！');
                  this.$router.push({
                    path: '/',
                  });
                } else {
                  this.$Message.error('网络错误，请稍后再试')
                }
              },
              (err) => {
                this.$Message.error(err.message);
              });
          }
        }

        this.showMessage(this.currentName);
        this.$Message.success('消息列表已刷新');
      },
      showMessage(name) {
        // console.log(name);
        this.currentName = name;
        // console.log(this.show_data)
        this.show_data = [];
        api.getMessages(
          (res) => {
            if (res.message === 'SUCCESS') {
              this.show_data = [];
              this.data1 = res.object;
              this.data_length = this.data1.length;

              this.readed_message = [];
              this.unread_message = [];
              this.loading = false
              for (let i = 0; i < this.data1.length; i++) {
                this.data1[i]['time'] = this.data1[i]['createdDateTime'].toString().replace(/T/g, ' ');
                if (this.data1[i].ifRead) {
                  // this.readed_message.push(this.data1[i]);
                  this.readed_message[this.readed_message.length] = this.data1[i];
                } else {
                  // this.unread_message.push(this.data1[i]);
                  this.unread_message[this.unread_message.length] = this.data1[i];
                }
              }
              if (name === '1') {
                this.readButton = false;
                this.delButton = false;
                this.show_data = [];
                this.to_show_data = [];
                // for (let i = 0; i < this.data1.length; i++) {
                //   let data = {
                //     'content': this.data1[i].content,
                //     'time': this.data1[i].createDateTime
                //   };
                //   this.show_data.push(data);
                // }
                // this.show_data = this.data1;
                this.to_show_data = this.data1;
                this.data_length = this.to_show_data.length;
                let loc = 5, i;
                this.show_data = [];
                if (loc > this.data_length) {
                  for (i = 0; i < this.data_length - loc + 5; i++) {
                    this.show_data[i] = this.to_show_data[loc - 5 + i];
                  }
                }
                else {
                  for (i = 0; i < 5; i++) {
                    this.show_data[i] = this.to_show_data[loc - 5 + i];
                  }
                }

              }
              else if (name === '2') {
                this.readButton = true;
                this.delButton = false;
                this.show_data = [];
                this.to_show_data = [];
                this.to_show_data = this.readed_message;
                this.data_length = this.readed_message.length;
                // console.log(this.readed_message);
                let loc = 5, i;
                this.show_data = [];
                if (loc > this.data_length) {
                  for (i = 0; i < this.data_length - loc + 5; i++) {
                    this.show_data[i] = this.to_show_data[loc - 5 + i];
                  }
                }
                else {
                  for (i = 0; i < 5; i++) {
                    this.show_data[i] = this.to_show_data[loc - 5 + i];
                  }
                }
              }
              else if (name === '3') {
                this.delButton = true;
                this.readButton = false;
                this.show_data = [];
                this.to_show_data = [];

                this.to_show_data = this.unread_message;
                this.data_length = this.unread_message.length;

                let loc = 5, i;
                this.show_data = [];
                if (loc > this.data_length) {
                  for (i = 0; i < this.data_length - loc + 5; i++) {
                    this.show_data[i] = this.to_show_data[loc - 5 + i];
                  }
                }
                else {
                  for (i = 0; i < 5; i++) {
                    this.show_data[i] = this.to_show_data[loc - 5 + i];
                  }
                }

              }
            } else if (res.message === 'WRONG') {
              this.$Message.error('网络错误，请稍后再试')
            } else if (res.message === 'FAIL') {
              this.$Message.error('网络错误，请稍后再试')
            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
          },
          (err) => {
            this.$Message.error(err.message);
          }
        )


      },
      getMessage() {
        api.getMessages(
          (res) => {
            if (res.message === 'SUCCESS') {

              this.data1 = res.object;
              this.data_length = this.data1.length;

              this.loading = false;
              this.readed_message = [];
              this.unread_message = [];
              for (let i = 0; i < this.data1.length; i++) {
                if (this.data1[i].read) {
                  // this.readed_message.push(this.data1[i]);
                  this.readed_message[this.readed_message.length] = this.data1[i];
                } else {
                  // this.unread_message.push(this.data1[i]);
                  this.unread_message[this.unread_message.length] = this.data1[i];
                }
              }
              this.changePage(this.page_loc);
            } else if (res.message === 'WRONG') {
              this.$Message.error('网络错误，请稍后再试')
            } else if (res.message === 'FAIL') {
              this.$Message.error('网络错误，请稍后再试')
            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
          },
          (err) => {
            this.$Message.error(err.message);
          }
        )
      },
      changePage(page) {
        // console.log(this.data1);
        this.loading = false;
        let loc = page * 5, i;
        this.show_data = [];
        if (loc > this.data_length) {
          for (i = 0; i < this.data_length - loc + 5; i++) {
            this.show_data[i] = this.to_show_data[loc - 5 + i];
          }
        }
        else {
          for (i = 0; i < 5; i++) {
            this.show_data[i] = this.to_show_data[loc - 5 + i];
          }
        }
        // console.log(this.data1);
        //console.log(this.show_data)
        // this.showMessage();
      }

    }
  }
</script>

<style scoped>

</style>
