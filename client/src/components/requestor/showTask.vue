<template>
  <div style="height:100%;">
    <div v-show="type ===1">
      <Content :style="{padding: '0 50px'}">
        <Breadcrumb :style="{margin: '20px 0'}">
          <BreadcrumbItem></BreadcrumbItem>
        </Breadcrumb>

        <div style="min-height: 200px;float:left">
          <Card style="width:100%;min-height:700px;" >
            <RadioGroup v-model="show_type" type="button" @on-change="changeType">
              <Radio label="已完成"></Radio>
              <Radio label="进行中"></Radio>
            </RadioGroup>
            <Table v-if="show_type==='已完成'" :loading="loading" :columns="columns1" :data="show_data" @on-row-click="click_table" ellipsis></Table>
            <Table v-else-if="show_type!=='已完成'" :loading="loading" :columns="columns2" :data="show_data" @on-row-click="click_table" ellipsis></Table>
            <div style="margin: 10px;overflow: hidden">
              <div style="float: right;">
                <Page :total="data_length" :current="page_loc" @on-change="changePage" simple></Page>
              </div>
            </div>
            <Modal
              v-model="modal1"
              title="是否结束此任务?"
              :closable="false"
              ok-text="确认"
              @on-ok="ok"
              @on-cancel="cancel">
              <p>ID:{{id}}</p>
              <p>任务描述:{{description}}</p>
            </Modal>
            <Modal
              v-model="modal2"
              title="任务详情"
              :closable="false"
              ok-text="确认"
              @on-ok="ok"
              @on-cancel="cancel">
              <p>ID:{{id}}</p>
              <p>任务描述:{{description}}</p>
              <p>点击确认查看任务标注信息</p>
            </Modal>
            <p slot="title">
              <Icon type="ios-film-outline"></Icon>
              任务列表
            </p>
            <ul>
            </ul>
          </Card>
        </div>
      </Content>
    </div>
    <div v-show="type !==1">
      <Card style="width:100%;height:100%">
        <Button slot="title" type="text"  @click="cancel" style="font-weight:bold">返回<<<</Button>
        <canvas-for-worker ref="canvasForWorker"></canvas-for-worker>
      </Card>
    </div>
  </div>
</template>

<script>
  import api from '../../api/requestor'
  import canvasForWorker from '../util/canvasForWorker'

    export default {
        name: "showTask",
        components:{canvasForWorker},
      data() {
        return {
          type:1,
          id:-1,
          show_type:'已完成',
          page_loc:1,
          description:'',
          MessageList: [],
          modal1: false,
          modal2:false,
          loading: true,
          data_length:0,
          mes:'',
          columns1: [
            {
              title: 'ID',
              key: 'id'
            },
            {
              title: '任务名称',
              key: 'taskName'
            },
            {
              title: '任务状况',
              key: 'finish',
              render: (h, params) => {
                const row = params.row;
                let color = row.people - row.remaining;
                color =( color / row.people )* 100
                if(row.finishTime)color  =100;
                return h('Progress', {
                  props: {
                    percent:color,
                    status:'active'
                  },
                  on: {
                    click: () => {
                      this.$Message.error(row.id)
                    }}});
              }
            },
            // {
            //   title: '任务描述',
            //   key: 'description'
            // },
            {
              title: '所需人数',
              key: 'people'
            },
            {
              title: '奖励点数',
              key: 'reward'
            },
            {
              title: '任务操作',
              key: 'action',
              width: 150,
              align: 'center',
              render: (h, params) => {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                      }
                    }
                  }, '查看任务'),
                ]);
              }
            }
          ],
          columns2: [
            {
              title: 'ID',
              key: 'id'
            },
            {
              title: '任务名称',
              key: 'taskName'
            },
            {
              title: '任务状况',
              key: 'finish',
              render: (h, params) => {
                const row = params.row;
                let color = row.people - row.remaining;
                color =( color / row.people )* 100
                color = color.toFixed(2)
                if(row.finishTime)color  =100;
                return h('Progress', {
                  props: {
                    percent:color,
                    status:'active'
                  },
                  on: {
                    click: () => {
                      this.$Message.error(row.id)
                    }}});
              }
            },
            {
              title: '所需人数',
              key: 'people'
            },
            {
              title: '奖励点数',
              key: 'reward'
            },
            {
              title: '任务操作',
              key: 'action',
              width: 150,
              align: 'center',
              render: (h, params) => {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.show(params.index)
                      }
                    }
                  }, '结束任务'),
                ]);
              }
            },
          ],
          data1: [
          ],
          data2:[],
          show_data:[],
          con_data:{},
        }
      },
      created(){
        this.getTask()
      },
      methods: {
        finishTask() {
          let data = {};
          data['taskId'] = this.id;
          api.finishTask(data,
            (res) => {
              console.log(res);
              if (res === 'SUCCESS') this.getTask();
              else if (res === 'WRONG') {
                this.$Message.error('网络错误，请稍后再试')
              } else if (res === 'FAIL') {
                this.$Message.error('网络错误，请稍后再试')
              }
              else if (res === 'TIMEOUT') {
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              }
            },
            (err) => {
              console.log(err);
              this.$Message.error(err.message);
            }
          )

        },
        ok () {
          if(this.show_type==='进行中'){
            this.mes = '结束任务'
            this.finishTask()
          }
          else{
            this.mes = '查看信息'
            this.type = 0;
            console.log(this.con_data)
            this.$refs.canvasForWorker.load_pic(this.con_data,-1)
          }

        },
        cancel () {
          console.log(this.columns2)
          this.changePage(1);
          this.type = 1;
        },
        click_table(data){
          // console.log(data.id)
          if(this.show_type==='进行中'){
            this.modal1 = true;
          }
          else {
            this.modal2 = true;
            for(let i in this.data1){
              if(this.data1[i].id === data.id){
                this.con_data = this.data1[i]
              }
            }

          }
          this.id = data.id;
          this.description = data.description
        },
        getTask() {
          api.getFinishTasks(
            (res) => {
              if (res.message === 'SUCCESS') {
                this.loading = false;
                this.data1 = res.object;
                console.log(this.data1)
                api.getOngoingTASKS(
                  (res) => {
                    if (res.message === 'SUCCESS') {
                      this.data2 = res.object;
                      this.data_length = this.data1.length;
                      this.show_data=[];
                      this.changePage(1);
                      this.loading = false;
                      //console.log(this.data_length)
                    } else if (res.message === 'WRONG') {
                      this.$Message.error('网络错误，请稍后再试')
                    } else if (res.message === 'FAIL') {
                      this.$Message.error('网络错误，请稍后再试')
                    }
                    else if(res.message === 'TIMEOUT'){
                      this.$Message.error('您因为太久未操作而被登出！');
                      this.$router.push({
                        path: '/',
                      });
                    }},
                  (err) => {
                    this.$Message.error(err.message);
                  }
                )
              } else if (res.message === 'WRONG') {
                this.$Message.error('网络错误，请稍后再试')
              } else if (res.message === 'FAIL') {
                this.$Message.error('网络错误，请稍后再试')
              }
              else if(res.message === 'TIMEOUT'){
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              }},
            (err) => {
              this.$Message.error(err.message);
            }
          )
        },
        changeType(){
          this.changePage(1)
        },
        changePage (page) {
          // The simulated data is changed directly here, and the actual usage scenario should fetch the data from the server
          let loc = page *10,i,data=this.data1;
          this.show_data = [];
          if(this.show_type==='进行中')data = this.data2;
          this.data_length = data.length;
          if(loc>this.data_length){
            for(i=0;i<this.data_length-loc+10;i++){

              this.show_data[i] = data[loc-10+i];
            }
          }
          else{
            for(i=0;i<10;i++){

              this.show_data[i] =data[loc-10+i];

            }
          }
        // console.log(this.show_data)
        }

      }

    }

</script>

<style scoped>

</style>
