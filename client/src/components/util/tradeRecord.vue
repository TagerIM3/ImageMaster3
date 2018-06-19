<template>
  <Card style="width:100%;">

    <p slot="title">
      <icon type="person"></icon>
      我的交易记录
    </p>
    <div style="font-size: 17px;font-weight: bolder">
      <p style="float:left">我的余额：</p><p style="float: left;">{{remain}}</p>
      <Button type="primary" style="position: relative;left:5%;" @click="toRecharge" v-if="isReq">前往充值</Button>
    </div>
    <br><br>
    <div v-if="showRecord">
      <Table :loading="loading" :data="show_data" :columns="tableColumns1" stripe></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
        </div>
      </div>
    </div>

    <Modal
      v-model="ifRecharge"
      title="兑换码充值"
      @on-ok="recharge"
      @on-cancel="">
      <i-input placeholder="请输入兑换码" style="width:100%" v-model="rechargeCode"></i-input>
      <!--<Button @click="recharge" type="primary">充值</Button>-->
    </Modal>
  </Card>
</template>

<script>
  import workerAPI from '../../api/worker'
  import requestorAPI from '../../api/requestor'
  import ResultMessage from '../../util/ResultMessage'
  export default {
    name: "tradeRecord",
    data() {
      return {
        showRecord:false,
        rechargeCode: '',
        ifRecharge:false,
        isReq: false,
        remain: 10.0,
        data_length: 0,
        loading: false,
        // to_show_data:[],
        show_data: [],
        data: [],
        indexOfPage: 1,
        tableColumns1: [
          {
            title: '流水号',
            key: 'id'
          },

          {
            title: '业务备注',
            key: 'item'
          },
          {
            title: '交易时间',
            key: 'time'
          },
          {
            title: '收支',
            key: 'in_out',
            render(h,params){
              // if(params.row.in_out.indexOf("+")>-1){
              //
              // }
              let s = params.row.in_out;
              return h('span',{
                style:{
                color:(params.row.in_out.indexOf("+")>-1)?"#00ff00":"#ff0000"
                }
              },s)
            }
          },
          {
            title: '余额',
            key: 'remaining'
          },
        ],
      }
    },
    mounted(){
      this.getRecord();
    },
    methods:{
      toRecharge(){
        this.rechargeCode = '';
        this.ifRecharge = true;
      },
      getRequestor(){
        workerAPI.getWorker((res)=>{
            if(res.message === ResultMessage.SUCCESS){

            }
            else if(res.message === ResultMessage.TIMEOUT){
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else{
              this.$Message.error('网络出错，请稍后再试');
            }
        },
          (err)=>{
          this.$Message.error(err.message);
          });
      },
      recharge(){
        requestorAPI.recharge(this.rechargeCode, (res)=>{
          if(res.message === ResultMessage.SUCCESS){
            this.$Message.success('充值成功');
            this.getRequestor();
            this.getRecord();
            // requestorAPI.getWorker((res)=>{
            //   if(res.message === ResultMessage.SUCCESS){
            //
            //   }
            //   else if(res.message === ResultMessage.TIMEOUT){
            //     this.$Message.error('您因为太久未操作而被登出！');
            //     this.$router.push({
            //       path: '/',
            //     });
            //   }
            //   else{
            //     this.$Message.error('网络错误，请稍后再试');
            //   }
            // },
            //   (err)=>{
            //     this.$Message.error(err.message);
            //   });
          }
          else if(res.message === ResultMessage.TIMEOUT){
            this.$Message.error('您因为太久未操作而被登出！');
            this.$router.push({
              path: '/',
            });
          }
          else{
            this.$Message.error('充值失败');
          }
        },
          (err)=>{
            this.$Message.error(err.message);
          });
      },
      getRecord(){
        workerAPI.getWorker((res)=>{
          if(res.message === ResultMessage.SUCCESS){
            let data = res.object;
            this.remain = data.points;
            if(data.userType === 'REQUESTOR'){
              this.isReq = true;
            }
            // this.changePage(this.indexOfPage);
            // console.log(res.object);
          }
          else if(res.message === ResultMessage.TIMEOUT){
            this.$Message.error('您因为太久未操作而被登出！');
            this.$router.push({
              path: '/',
            });
          }
          else{
            this.$Message.error('网络错误，请稍后再试');
          }
        },
          (err)=>{
          this.$Message.error(err.message);
        });
        workerAPI.getPointEntries((res)=>{
            console.log(res);
          if(res.message === 'SUCCESS'){
            this.data = res.object;
            this.data_length = this.data.length;
            this.changePage(this.indexOfPage);
            this.showRecord =true;
          }
          else if(res.message ==='TIMEOUT'){
            this.$Message.error('您因为太久未操作而被登出！');
            this.$router.push({
              path: '/',
            });
          }
          else{
            this.$Message.error('网络错误，请稍后再试');
          }
        },
          (err)=>{
          this.$Message.error(err.message);
          });
      },

      changePage(pageSize) {
        let loc = pageSize * 5;
        let i;
        this.show_data = [];
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

        for(let i=0;i<this.show_data.length;i++){
          this.show_data[i]['time']=this.show_data[i]['createDateTime'].toString().replace(/T/g,' ');
          this.show_data[i].remaining = this.show_data[i].remaining.toFixed(2);
          if(this.show_data[i].plus){
            this.show_data[i]['in_out']='+'+this.show_data[i].num.toFixed(2);
          }else{
            this.show_data[i]['in_out']='-'+this.show_data[i].num.toFixed(2);
          }
        }

        // let loc = page * 10, i;
        // this.show_data = [];
        // if (loc > this.data_length) {
        //   for (i = 0; i < this.data_length - loc + 10; i++) {
        //     this.show_data[i] = this.data1[loc - 10 + i];
        //   }
        // }
        // else {
        //   for (i = 0; i < 10; i++) {
        //     this.show_data[i] = this.data1[loc - 10 + i];
        //   }
        // }
        //
        // for(let i =0;i<this.show_data.length;i++){
        //   let data = {
        //     'id': this.show_data[i].id,
        //     'item': this.show_data[i].item,
        //     'date': '',
        //     'remaining': this.show_data[i].remaining
        //   };
        //   if(this.show_data[i].plus){
        //     data.push({
        //       'num': '+'+this.show_data[i].num.toString()
        //     })
        //   }else{
        //     data.push({
        //       'num': '-'+this.show_data[i].num.toString()
        //     })
        //   }
        //
        //   this.to_show_data.push(data);
        //
        // }

      }
    }

  }
</script>

<style scoped>

</style>
