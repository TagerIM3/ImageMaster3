<template>
  <div>
    <Content :style="{padding: '0 50px'}">
      <Breadcrumb :style="{margin: '20px 0'}">
        <BreadcrumbItem></BreadcrumbItem>
      </Breadcrumb>

      <div style="min-height:480px">


        <Form  ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80" :label-position="left" style="padding-right: 20px">
          <Card style="min-height:580px;width: 100%">
            <!--<div style="position: absolute;padding-left: 50%">-->
              <!--{{this.$parent.$parent.$parent.$parent.user.points}}-->
            <!--</div>-->
            <p slot="title">发布任务</p>
            <div style="padding-left:25%;padding-right: 35%;">
              <FormItem label="任务名" prop="taskName">
                <i-input clearable type="text" placeholder="请输入任务名" v-model="formValidate.taskName" ></i-input>
              </FormItem>
              <FormItem label="参与人数" prop="people">
                <div style="">
                  <Poptip trigger="hover" content="最大人数为100" placement="top" style="width:100%">
                  <InputNumber  :max="100" :min="1" :step="1" v-model="formValidate.people" style="width:100%" @on-change="change_cost"></InputNumber>
                  </Poptip>
                </div>
              </FormItem>
              <FormItem label="基础奖励" prop="basePoints">
                <div style="width:100%">
                  <Poptip trigger="hover" :content="show_point" placement="top" style="width:100%">
                  <InputNumber  :max="100" :min="0" :step="0.25" v-model="formValidate.basePoints" style="width:100%" @on-change="change_cost"></InputNumber>
                  </Poptip>
                </div>
              </FormItem>
              <FormItem label="标签" prop="tags">
                <div style = "">
                  <Poptip trigger="hover" content="请选择或输入标签类别" placement="top" style="width:100%">
                    <Select v-model="formValidate.label" filterable @on-query-change="add_label" >
                      <Option v-for="item in label_list" :value="item" :key="item">{{ item }}</Option>
                    </Select>
                  </Poptip>

                </div>
              </FormItem>

              <FormItem label="任务描述" prop="description">
                <div>
                  <i-input clearable type="textarea" placeholder="任务描述" v-model="formValidate.description" style="padding-left: 0%;width:100%" size="large" :rows = '4'></i-input>
                </div>
              </FormItem>

              <div style="padding-left: 22%;width: 100%">
                <uploadFile ref = "upload" ></uploadFile>
              </div>


            </div>


            <div style="padding-left: 42%;">
              <div style="float:left">
                <Poptip trigger="hover" title="提示" content="请确认输入信息已完整" placement="top" >
                  <i-button type="primary" icon="checkmark-round" align="center" @click="show_info" >发起任务</i-button>
                </Poptip>
              </div>

              <!--<div style="float:left;padding-left:30px">-->
              <!--<Poptip trigger="hover" title="提示" content="清除已输入的所有内容，包括压缩包" placement="top" >-->
              <!--<i-button type="ghost" icon="close-round"  @click="reset('formValidate')" >清除输入</i-button>-->
              <!--</Poptip>-->
              <!--</div>-->
            </div>

          </Card>
        </Form>
        <ul>
        </ul>
      </div>
      <Modal
        v-model="if_show"
        title="是否发起此任务?"
        :closable="false"
        ok-text="确认"
        @on-ok="submit('formValidate')"
        @on-cancel="cancel">
        <p>此任务将花费:{{cost_1}}积分</p>
        <p>其中平台抽成{{cost_2}}积分</p>
      </Modal>
    </Content>
  </div>
</template>

<script>
  import  uploadFile from '../util/uploadFile'
  import  autoComp from '../util/autoComp'
  import api from '../../api/requestor'
  export default {
    components: {uploadFile,autoComp},
    name: "postTask",

    data () {
      return {
        label_list:["人物","车辆","动物","风景",'其他'],
        show_point:"",
        if_show:0,
        tip_cost:"此次任务消耗积分:11",
        cost_1:11,
        formValidate: {
          taskName: '',
          people: 10,
          basePoints: 1,
          tags: {},
          label:'',
          description: '',
          image: '',
          endTime:'',

        },
        ruleValidate: {
          taskName: [
            { required: true, message: '任务名不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true,  message: '任务描述不能为空', trigger: 'blur' }
          ],


        },
        date_options: {
          disabledDate (date) {
            return date && date.valueOf() < Date.now() - 86400000 ;
          }
        },
      }
    },
    mounted(){

      api.getRequestor(
        (res)=>{

          if (res.message === 'SUCCESS') {
            this.$parent.$parent.$parent.$parent.user = res.object
            console.log( this.$parent.$parent.$parent.$parent.user.points)
            this.show_point = "您的剩余点数:" + this.$parent.$parent.$parent.$parent.user.points;
            this.cost_1 = 11;
            this.cost_2 = 1;
            this.tip_cost="此次任务消耗积分:"+this.cost_1;
          }
          else if(res.message === 'TIMEOUT'){
            this.$Message.error('您因为太久未操作而被登出！')
            this.$router.push({
              path: '/',
            });
          }
        },
        (err) => {
          //console.log(err)
          this.$Message.error(err.message);
        }

      )
    },
    methods:{
      show_info(){
        this.$refs['formValidate'].validate((valid) =>{
          if(valid){
            this.if_show = 1;
          }
          else{
            this.$Message.error("请输入完整的信息！")
          }
        })
      },
      change_cost(){
        if(this.formValidate.people<20){
          this.cost_1 = this.formValidate.people * (0.1+this.formValidate.basePoints)
          this.cost_2 = this.formValidate.people * 0.1;
          this.cost_1 = Math.ceil( this.cost_1)
          this.cost_2 = Math.ceil( this.cost_2)
        }
        else if(this.formValidate.people<50){
          this.cost_1 = this.formValidate.people * (0.15+this.formValidate.basePoints)
          this.cost_2 = this.formValidate.people * 0.15;
          this.cost_1 = Math.ceil( this.cost_1)
          this.cost_2 = Math.ceil( this.cost_2)
        }
        else {
          this.cost_1 = this.formValidate.people * (0.2+this.formValidate.basePoints)
          this.cost_2 = this.formValidate.people * 0.2;
          this.cost_1 = Math.ceil( this.cost_1)
          this.cost_2 = Math.ceil( this.cost_2)
        }
        this.tip_cost="此次任务消耗积分:"+this.cost_1;
      },
      add_label(query){
        // this.formValidate.label = ""
        // console.log(this.formValidate.label)
        // this.label_list[4] = query;
        // if(query === "") this.formValidate.label = ""
        // else this.formValidate.label = this.label_list[4];
        // console.log(this.formValidate)
      },
      submit(name){
        // console.log(this)
        // console.log(this.$parent.$parent.$parent.$parent.user)
        if (typeof(this.formValidate.label) == "undefined")
        {
          this.formValidate.label = "";
        }
        console.log(this.formValidate.basePoints*this.formValidate.people)
        if(!this.formValidate.people)this.formValidate.people = 10;
        if(!this.formValidate.basePoints)this.formValidate.basePoints = 1;
        if(this.$parent.$parent.$parent.$parent.user.points < this.cost_1){
          this.$Message.error("用户点数不足！");
        }
        else
        this.$refs[name].validate((valid) => {

          let file_name_size = this.$refs.upload.file.name.length;
          if (this.formValidate.label!=='' && valid && this.$refs.upload.file && this.$refs.upload.file.size<1024*1024*100 && this.$refs.upload.file.name.substring(file_name_size-4,file_name_size).toUpperCase() === ".ZIP") {
            //this.$Message.success(this.$refs.upload.file.name.substring(this.$refs.upload.file.name.size-4,this.$refs.upload.file.name.size).toLowerCase());
            // this.$refs.upload.upload();
            let taskPre = {
              'taskName': this.formValidate.taskName,
              'description': this.formValidate.description,
              'label': this.formValidate.label,
              'people': this.formValidate.people,
              'reward': this.formValidate.basePoints,
              'consume':this.cost_1
            };
            // console.log(taskPre)
            api.submitTask(taskPre,(res) => {
                //console.log(res)
                if (res === 'SUCCESS') {

                  this.show_point = "您的剩余点数:" + this.$parent.$parent.$parent.$parent.user.points;
                  this.$Message.success("任务信息已提交，正在上传压缩包，请勿切换界面");
                  api.uploadImages(this.$refs.upload.file,(res) => {
                      this.$parent.$parent.$parent.$parent.user.points = this.$parent.$parent.$parent.$parent.user.points - this.cost_1;
                      // console.log(res)
                      this.show_point = "您的剩余点数:" + this.$parent.$parent.$parent.$parent.user.points;
                      if (res === 'SUCCESS') {
                        this.$Message.success("压缩包提交成功");
                        this.reset('formValidate');
                      }
                      else if (res === 'FAIL') {
                        this.$Message.error('压缩包上传失败，请稍后再试')
                      }
                      else if (res === 'NO_IMAGE') {
                        this.$Message.error('压缩包内不含图片，请重新选择！')
                      }
                      else{
                        this.$Message.error('提交压缩包失败，请稍后再试')
                      }
                    },
                    (err) => {
                      //console.log(err)
                      this.$Message.error(err.message);
                    })

                }
                else if (res === 'TIMEOUT'){
                  this.$Message.error('您因为太久未操作而被登出！');
                  this.$router.push({
                    path: '/',

                  });
                }
                else if (res === 'FAIL') {
                  this.$Message.error('提交任务失败，请稍后再试')
                }
                else if(res === 'EXIST'){
                  this.$Message.error('该任务名称已被使用，请重新输入')
                }
                else if(res === 'LESS'){
                  this.$Message.error('您的积分点数不足！')
                }
                else{
                  this.$Message.error('上传任务失败，请稍后再试')
                }
              },
              (err) => {

                this.$Message.error(err.message);
              })



          }
          else if(this.formValidate.label===''){
            this.$Message.error('请添加标注');
          }
          else if((this.$refs.upload.file.name.substring(file_name_size-4,file_name_size).toUpperCase()!==".ZIP")){
            this.$Message.error('选择的文件不是压缩文件');
          }
          else if(!this.$refs.upload.file){
            this.$Message.error('请选择需要上传的压缩包！');
          }
          else if(!(this.$refs.upload.file.size<1024*1024*100)){
            this.$Message.error('文件大于100MB！');
          }
          else{
            this.$Message.error('请检查输入是否有误');
          }
        })
      },
      reset(name) {
        // console.log(this.$refs.upload.file)


        //this.$refs.upload.upload();
        this.$refs[name].resetFields();
        this.$refs.upload.file = null;
        this.formValidate.label = "";

      },
    }
  }
</script>

<style scoped>

</style>
