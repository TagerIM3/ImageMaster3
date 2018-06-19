<template>
    <div>
      <Content :style="{padding: '24px 0', minHeight: '480px', background: '#fff'}">
        <Layout>
          <Sider hide-trigger :style="{background: '#fff'}">
            <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']" accordion>
              <Submenu name="1">
                <template slot="title">
                  <Icon type="ios-navigate"></Icon>
                  选择颜色
                </template>
                <ColorPicker style="padding-left: 35%" v-model="color" size="large" alpha/>
              </Submenu>
              <Submenu name="2" v-if="type===1">
                <template slot="title">
                  <Icon type="ios-keypad"></Icon>
                  标注方式
                </template>
                <MenuItem name="2-1"  @click.native="changeModel(1)">全局标注</MenuItem>
                <MenuItem name="2-2"  @click.native="changeModel(2)">方框标注</MenuItem>
                <MenuItem name="2-3"  @click.native="changeModel(3)">区域标注</MenuItem>
              </Submenu>
              <Submenu name="3" v-if="type===1">
                <template slot="title">
                  <Icon type="ios-analytics"></Icon>
                  任务选项
                </template>
                <MenuItem name="3-1" @click.native="if_save_show=1">暂存任务</MenuItem>
                <MenuItem name="3-2" @click.native="if_submit_show=1">提交任务</MenuItem>
              </Submenu>
            </Menu>
          </Sider>
          <Content :style="{padding: '24px', minHeight: '280px', background: '#fff'}">


            <!--<br>-->

            <div>
              <canvas id="canvas_back" :width="canvas_width" :height="canvas_height" style="border:1px solid black;position:absolute"></canvas>
              <canvas id="canvas_front" :width="canvas_width" :height="canvas_height" style="border:1px solid black;position:absolute"
                      @mousedown="canvasDown($event)"
                      @mouseup="canvasUp($event)"
                      @mousemove="canvasMove($event)"
                      @touchstart="canvasDown($event)"
                      @touchend="canvasUp($event)"
                      @touchmove="canvasMove($event)"
              ></canvas>
            </div>
            <div style="padding-left: 58%;">
              <Card style="max-width: 600px">
                <p slot="title">标签  当前标注模式:{{tagModel}}</p>
                <div style=" border-bottom:1px solid #eee;min-height: 50px">
                  <i-input v-if="type===1" v-model="add_tags" :placeholder="place_h" style="padding-left: 5%;width: 75%" :disabled="input_dis"></i-input>
                  <p v-else style="font-weight: bold;text-align: center">当前标注：{{add_tags}}</p>
                  <!--<i-input v-model="add_tags" :placeholder="place_h" style="padding-left: 5%;width: 75%" :disabled="input_dis"></i-input>-->
                  <br v-if="type===1"><br >
                  <button-group v-if="type===1" style="padding-left:5%;padding-bottom: 5px;" >
                    <Button size="small" type="primary" @click="select_pic = true">选择图片</Button>
                    <Button size="small"  type="info" @click="tag_input">{{type_input}}</Button>
                    <Button size="small"  type="warning" @click="tag_update">修改标注</Button>
                    <Button size="small" type="error" @click="clearArea">清空画布</Button>

                  </button-group>
                  <button-group v-if="type!==1" style="padding-left:30%;padding-bottom: 5px;" >
                    <Button size="small" type="primary" @click="select_pic = true">选择图片</Button>
                    <Button size="small" type="error" @click="clearArea">清空画布</Button>

                  </button-group>

                </div>
                <Tag v-if="show_tag" type="border" color="blue" v-for="item in tag_entire_list[loc]" :key="item" :name="item.tag" closable @on-close="handleClose2" @click.native="entire_tag_show(item)">{{item.tag}}</Tag>
                <Tag v-if="show_tag" type="border" color="red" v-for="item in tag_box_list[loc]" :key="item" :name="item.loc" closable @on-close="handleClose3" @click.native="reDraw(1,item)">{{item.tag}}</Tag>
                <Tag v-if="show_tag" type="border" color="green" v-for="item in tag_area_list[loc]" :key="item" :name="item.loc" closable @on-close="handleClose4" @click.native="reDraw(0,item)">{{item.tag}}</Tag>

              </Card>
            </div>
          </Content>
        </Layout>
        <Modal
          v-model="if_save_show"
          title="是否暂存此任务?"
          :closable="false"
          ok-text="确认暂存任务"
          @on-ok="save_task"
          @on-cancel="cancel">
        </Modal>
        <Modal
          v-model="if_submit_show"
          title="是否提交此任务?"
          :closable="false"
          ok-text="确认提交任务"
          @on-ok="submit_task"
          @on-cancel="cancel">
        </Modal>
      </Content>


      <div>
        <Modal
          title="图片列表" v-model="select_pic" cancel-text="" ok-text="返回"
          :styles="{top: '20px'}">
          <div class="demo-upload-list" v-for="item in picture_list">
            <template>
              <img :src="item.url">
              <div class="demo-upload-list-cover">
                <Icon type="ios-eye-outline" @click.native="handleView(item.url,item.index)"></Icon>
              </div>
            </template>
          </div>
        </Modal>
        <Modal title="查看大图" v-model="visible" :styles="{top: '20px'}"  @on-ok="ok" ok-text="确认">
          <img :src="'' + imgPath " v-if="visible" style="width: 100%">
        </Modal>
      </div>


    </div>
</template>

<script>
  import api from "../../api/worker"
    export default {
      name: "canvasForWorker",
      data(){
          return{
            tagModel:'全局标注',
            type_input:"添加标注",
            color:'rgba(255, 0,0, .5)',
            tag_entire_list:[],
            tag_box_list:[],
            tag_area_list:[],
            point_box_list:[],
            point_area_list:[],
            picture_list:[
            //   {
            //     index:0,
            //   'url': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
            // },
            //   {
            //     index:1,
            //     'url': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'
            //   },{
            //     index:2,
            //     'url': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
            //   },
            //   {
            //     index:3,
            //     'url': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'}
              ],
            work:{},
            if_submit_show:0,
            if_save_show:0,
            canvas_height:0,
            canvas_width:0,
            imgPath:'',
            visible: false,
            select_pic:false,
            canvas_back:'',
            canvas_front:'',
            cxt_back:'',
            cxt_front:'',
            loc:-1,
            add_tags:"",
            show_tag:true,
            canvasMoveUse: false,
            x:'',
            y:'',
            targetx:'',
            targety:'',
            ifDraw:false,
            count:0,
            x_list:[],
            y_list:[],
            check_mode:false,
            task_id:-1,
            update_loc:-1,
            work:{},
            type:-1,
            time_count:-1,
            input_dis:false,
            place_h:"请输入标注",
        }
      },
      mounted: function () {
        this.canvas_back = document.getElementById('canvas_back');
        this.canvas_front = document.getElementById('canvas_front');
        this.cxt_front = this.canvas_front.getContext('2d');
        this.cxt_back = this.canvas_back.getContext('2d');
        for (let i = 0; i < this.picture_list.length; i++) {
          this.tag_entire_list[i] = [];
          this.tag_box_list[i] = [];
          this.tag_area_list[i] = [];
          this.point_box_list[i] = [];
          this.point_area_list[i] = []
        }
        this.canvas_height=300
        this.canvas_width=400
        // this.load_pic
        this.A()
        this.input_dis
      },
      methods:{
        A () {
          let that = this;
          setInterval(function(){
            that.count_time();
          },1000)
        },
        count_time(){
          if(true){
            this.time_count=this.time_count+1
            // this.$Message.success(this.time_count+"")
            // setInterval(this.count_time(),1000)
            // this.count_time()
            // setTimeout(this.count_time(),1000)
          }
        },
        submit_task(){
          let par = this.$parent.$parent
          // console.log(par)
          // par.flag = 0;
          let num,flag=true;
          for(let i=0;i<this.picture_list.length;i++){
            num = this.tag_entire_list[i].length+this.tag_area_list[i].length+this.tag_box_list[i].length
            if(num===0)flag = false;
          }
          if(!flag){
            this.$Message.error("请确认已对所有图片进行标注！")
          }

          else{
            for(let i=0;i<this.work.images.length;i++){
              this.work.marks[i].words = [];
              this.work.marks[i].areas = []
              this.work.marks[i].boxes = []

              for(let j=0;j<this.tag_entire_list[i].length;j++){
                this.work.marks[i].words[j] = this.tag_entire_list[i][j].tag
              }
              for(let j=0;j<this.tag_box_list[i].length;j++){
                this.work.marks[i].boxes[j] = {
                  "x": this.point_box_list[i][j].x,
                  "y": this.point_box_list[i][j].y,
                  "targetX": this.point_box_list[i][j].targetx,
                  "targetY": this.point_box_list[i][j].targety,
                  "word": this.tag_box_list[i][j].tag,
                  "color": "1"
                }
              }
              for(let j=0;j<this.tag_area_list[i].length;j++){
                this.work.marks[i].areas[j] = {
                  "xlist": this.point_area_list[i][j].x_list,
                  "ylist": this.point_area_list[i][j].y_list,
                  "word": this.tag_area_list[i][j].tag,
                  "color": "1"
                }
              }
            }
            // console.log(this.work)
            // console.log(this.time_count)
            this.work.minute = (this.time_count/60)
            let data = {'work':this.work}
            api.submitWork(data,
              (res)=>{
              // console.log(res)
                if(res ==="SUCCESS"){
                  this.$Message.success('提交成功！');
                  console.log(par.flag)
                  par.flag = 0;
                  par.showTasks()
                  // console.log(par.flag)
                }
                else if (res === 'TIMEOUT') {
                  this.$Message.error('您因为太久未操作而被登出！');
                  this.$router.push({
                    path: '/',
                  });
                }
                else if(res === 'FAIL' ){
                  this.$Message.error('网络错误，请稍后再试！');
                }
                else if(res === 'FINISH' ){
                  this.$Message.error('该任务已结束，无法提交！');
                }
              else{
                  this.$Message.error('网络错误，请稍后再试！');
                }
              },
              (err)=>{
                this.$Message.error(err);
              }
            )
          }

          // let data = {'work':this.$parent.work}

        },
        save_task(){

          for(let i=0;i<this.work.images.length;i++){
            this.work.marks[i].words = [];
            this.work.marks[i].areas = []
            this.work.marks[i].boxes = []

            for(let j=0;j<this.tag_entire_list[i].length;j++){
              this.work.marks[i].words[j] = this.tag_entire_list[i][j].tag
            }
            for(let j=0;j<this.tag_box_list[i].length;j++){
              this.work.marks[i].boxes[j] = {
                "x": this.point_box_list[i][j].x,
                "y": this.point_box_list[i][j].y,
                "targetX": this.point_box_list[i][j].targetx,
                "targetY": this.point_box_list[i][j].targety,
                "word": this.tag_box_list[i][j].tag,
                "color": "1"
              }
            }
            for(let j=0;j<this.tag_area_list[i].length;j++){
              this.work.marks[i].areas[j] = {
                "xlist": this.point_area_list[i][j].x_list,
                "ylist": this.point_area_list[i][j].y_list,
                "word": this.tag_area_list[i][j].tag,
                "color": "1"
              }
            }
          }
          this.work.minute = this.time_count/60
          // console.log(this.work)
          let data = {'work':this.work}
          // console.log(data)
          api.saveWork(data,
            (res)=>{
              if(res ==="SUCCESS"){
                this.$Message.success('保存成功！');
              }
              else if (res === 'TIMEOUT') {
                this.$Message.error('您因为太久未操作而被登出！');
                this.$router.push({
                  path: '/',
                });
              }
              else{
                this.$Message.error('网络错误，请稍后再试！');
              }
            },
            (err)=>{
              this.$Message.error(err);
            }
          )

        },
        load_pic(work,type){

          this.type = type;
          this.work = work;
          this.count = 0;
          this. picture_list = []
          this.tag_entire_list=[]
          this.tag_box_list=[]
          this.tag_area_list=[]
          this.point_box_list=[]
          this.point_area_list=[]
          this.time_count = this.work.minute*60
          this.A
          if(type!==1){
            this.input_dis = true;
            this.place_h = "在此查看标注信息"
          }
          // markIntegrations
          if(type === -1){
            console.log(this.work.markIntegrations)
            for(let i=0;i<this.work.images.length;i++){
              this.tag_entire_list[i]=[]
              this.tag_box_list[i]=[]
              this.tag_area_list[i]=[]
              this.point_box_list[i]=[]
              this.point_area_list[i]=[]
              this.picture_list[i] = {
                "url":"http://106.14.178.184:8080/im3"+this.work.images[i],
                index:i
              }
              for(let j=0;j<this.work.markIntegrations[i].words.length;j++){
                this.tag_entire_list[i][j]={
                  "loc":this.count,
                  "tag":this.work.markIntegrations[i].words[j]
                }
                this.count++;
              }
              for(let j=0;j<this.work.markIntegrations[i].boxes.length;j++){
                this.tag_box_list[i][j]={
                  "loc":this.count,
                  "tag":this.work.markIntegrations[i].boxes[j].word
                }
                this.point_box_list[i][j]={
                  "x":this.work.markIntegrations[i].boxes[j].x,
                  "y":this.work.markIntegrations[i].boxes[j].y,
                  "targetx":this.work.markIntegrations[i].boxes[j].targetX,
                  "targety":this.work.markIntegrations[i].boxes[j].targetY,
                }

                this.count++;
              }
              // console.log(this.tag_box_list)
              for(let j=0;j<this.work.markIntegrations[i].areas.length;j++){
                this.tag_area_list[i][j]={
                  "loc":this.count,
                  "tag":this.work.markIntegrations[i].areas[j].word
                }
                this.point_area_list[i][j] ={
                  "x_list":this.work.markIntegrations[i].areas[j].xlist,
                  "y_list":this.work.markIntegrations[i].areas[j].ylist
                }
                this.count++;
              }
            }
          }
          else for(let i=0;i<this.work.images.length;i++){
            this.tag_entire_list[i]=[]
            this.tag_box_list[i]=[]
            this.tag_area_list[i]=[]
            this.point_box_list[i]=[]
            this.point_area_list[i]=[]
            this.picture_list[i] = {
              "url":"http://106.14.178.184:8080/im3"+this.work.images[i],
              index:i
            }
            for(let j=0;j<this.work.marks[i].words.length;j++){
              this.tag_entire_list[i][j]={
                "loc":this.count,
                "tag":this.work.marks[i].words[j]
              }
              this.count++;
            }
            for(let j=0;j<this.work.marks[i].boxes.length;j++){
              this.tag_box_list[i][j]={
                "loc":this.count,
                "tag":this.work.marks[i].boxes[j].word
              }
              this.point_box_list[i][j]={
                "x":this.work.marks[i].boxes[j].x,
                "y":this.work.marks[i].boxes[j].y,
                "targetx":this.work.marks[i].boxes[j].targetX,
                "targety":this.work.marks[i].boxes[j].targetY,
              }

              this.count++;
            }
            // console.log(this.tag_box_list)
            for(let j=0;j<this.work.marks[i].areas.length;j++){
              this.tag_area_list[i][j]={
                "loc":this.count,
                "tag":this.work.marks[i].areas[j].word
              }
              this.point_area_list[i][j] ={
                "x_list":this.work.marks[i].areas[j].xlist,
                "y_list":this.work.marks[i].areas[j].ylist
              }
              this.count++;
            }
          }

          console.log(this.picture_list)

        },
        tag_update(){
          if(this.check_mode){
            if(this.tagModel==="全局标注"){
              let p=-1;
              for(let i=0;i<this.tag_entire_list[this.loc].length;i++){
                if(this.tag_entire_list[this.loc][i].loc ===this.update_loc)p = i;
              }
              this.tag_entire_list[this.loc][p].tag = this.add_tags
            }
            else if(this.tagModel==='方框标注'){
              let p=-1;
              for(let i=0;i<this.tag_box_list[this.loc].length;i++){
                if(this.tag_box_list[this.loc][i].loc === this.update_loc.loc)p = i;
              }
              this.tag_box_list[this.loc][p].tag = this.add_tags
            }
            else{
              let p=-1;
              for(let i=0;i<this.tag_area_list[this.loc].length;i++){
                if(this.tag_area_list[this.loc][i].loc ===this.update_loc.loc)p = i;
              }
              // console.log(this.tag_area_list[this.loc][p])
              this.tag_area_list[this.loc][p].tag = this.add_tags
            }
          }
          else{
            this.$Message.error("当前未选中标记")
          }
        },
        entire_tag_show(loc){
          this.clearArea();
          this.update_loc = loc.loc;
          this.check_mode = true;
          this.add_tags = loc.tag
          this.tagModel = "全局标注"
          this.type_input="添加标注"

        },
        reDraw(type,loc){
          this.check_mode = true;
          this.type_input="添加标注"
          this.clearArea();
          let index = -1;
          this.update_loc = loc;
          this.setCanvasStyle()
          if(type){
            for(let i=0;i<this.tag_box_list[this.loc].length;i++){
              if(this.tag_box_list[this.loc][i].loc === loc.loc) {
                index = i;}
            }
            this.tagModel = "方框标注"
            this.cxt_front.beginPath();
            // console.log(this.point_box_list)
            this.cxt_front.moveTo(this.point_box_list[this.loc][index].x, this.point_box_list[this.loc][index].y)
            this.cxt_front.lineTo(this.point_box_list[this.loc][index].x, this.point_box_list[this.loc][index].targety);
            this.cxt_front.lineTo(this.point_box_list[this.loc][index].targetx,this.point_box_list[this.loc][index].targety);
            this.cxt_front.lineTo(this.point_box_list[this.loc][index].targetx, this.point_box_list[this.loc][index].y);
            this.cxt_front.lineTo(this.point_box_list[this.loc][index].x, this.point_box_list[this.loc][index].y);
            this.cxt_front.lineTo(this.point_box_list[this.loc][index].x, this.point_box_list[this.loc][index].targety);
            this.cxt_front.fill();
            this.cxt_front.stroke()
            this.add_tags = this.tag_box_list[this.loc][index].tag
          }
          else{
            this.tagModel = "区域标注"
            for(let i=0;i<this.tag_area_list[this.loc].length;i++){
              if(this.tag_area_list[this.loc][i].loc === loc.loc) {
                index = i;}
            }
            this.cxt_front.beginPath();
             // console.log(this.point_area_list[this.loc][index])
            this.cxt_front.moveTo(this.point_area_list[this.loc][index].x_list[0], this.point_area_list[this.loc][index].y_list[0])
            let i
            for(i=1;i<this.point_area_list[this.loc][index].x_list.length;i++){
              this.cxt_front.lineTo(this.point_area_list[this.loc][index].x_list[i], this.point_area_list[this.loc][index].y_list[i]);
              //console.log(this.cxt_front)
              this.cxt_front.stroke()
            }
            this.add_tags = this.tag_area_list[this.loc][index].tag
            this.cxt_front.fill();
            this.cxt_front.stroke()

          }

        },
        canvasMove (e) {
          if(this.type === 1){
            if (this.canvasMoveUse) {
              if(this.tagModel==='区域标注'){
                // const t = e.target;

                var bbox = this.canvas_front.getBoundingClientRect();
                var x = e.clientX - bbox.left * (this.canvas_front.width / bbox.width);
                var y = e.clientY - bbox.top * (this.canvas_front.height / bbox.height);

                let canvasX;
                let canvasY;
                canvasX = x
                canvasY = y
                this.x_list[this.x_list.length] = canvasX;
                this.y_list[this.y_list.length] = canvasY;
                this.cxt_front.lineTo(canvasX, canvasY);
                this.cxt_front.stroke()
              }
              else{
                // const t = e.target;
                var bbox = this.canvas_front.getBoundingClientRect();
                var x = e.clientX - bbox.left * (this.canvas_front.width / bbox.width);
                var y = e.clientY - bbox.top * (this.canvas_front.height / bbox.height);

                let canvasX;
                let canvasY;
                canvasX = x
                canvasY = y
                this.canvas_front.height = 0 + this.canvas_front.height;
                this.setCanvasStyle()
                this.cxt_front.lineTo(this.x, canvasY);
                this.cxt_front.lineTo(canvasX, canvasY);
                this.cxt_front.lineTo(canvasX, this.y);
                this.cxt_front.lineTo(this.x, this.y);
                this.cxt_front.lineTo(this.x, canvasY);
                this.cxt_front.stroke();
              }
            }
          }

        },
        canvasUp (e) {
          if(this.type === 1){
            var bbox = this.canvas_front.getBoundingClientRect();
            var x = e.clientX - bbox.left * (this.canvas_front.width / bbox.width);
            var y = e.clientY - bbox.top * (this.canvas_front.height / bbox.height);

            let canvasX;
            let canvasY;
            canvasX = x
            canvasY = y
            if(this.tagModel==='方框标注'){
              this.targetx = canvasX
              this.targety = canvasY
            }
            else if(this.tagModel==='区域标注'){

            }
            this.canvasMoveUse = false;
            this.cxt_front.fill();
            this.ifDraw = true;
          }
          // const t = e.target;

        },

        canvasDown (e) {
          if(this.type === 1){
            this.check_mode = false;
            this.type_input="添加标注"
            this.ifDraw = false;
            if(this.tagModel==='全局标注'){}
            else{
              this.x_list = [];
              this.y_list = [];
              this.clearArea();
              this.canvasMoveUse = true;
              var bbox = this.canvas_front.getBoundingClientRect();
              var x = e.clientX - bbox.left * (this.canvas_front.width / bbox.width);
              var y = e.clientY - bbox.top * (this.canvas_front.height / bbox.height);

              const canvasX = x;
              const canvasY = y;
              this.x_list[0] = canvasX;
              this.y_list[0] = canvasY;
              this.setCanvasStyle();
              this.x=canvasX;this.y=canvasY
              this.cxt_front.beginPath();
              this.cxt_front.moveTo(canvasX, canvasY)
            }
          }

        },
        setCanvasStyle () {
          this.cxt_front.lineWidth = 1;
          this.cxt_front.shadowColor = this.color;
          this.cxt_front.strokeStyle = this.color;
          this.cxt_front.fillStyle = this.color
        },

        clearArea() {
          this.type_input="添加标注"
          this.cxt_front.setTransform(1, 0, 0, 1, 0, 0);
          this.cxt_front.clearRect(0, 0, this.cxt_front.canvas.width, this.cxt_front.canvas.height);
          this.add_tags=""
          this.ifDraw=false;
        },


        tag_input(){
          if( this.type_input==="添加标注")this.check_mode = false;
          if(this.check_mode){
            this.$Message.error("查看模式下无法新增标注！")
          }
          else {
            // console.log(this.tag_entire_list[this.loc].indexOf(""+this.add_tags))
            let flag;
            for(let i in this.tag_entire_list[this.loc])if(this.tag_entire_list[this.loc][i].tag === this.add_tags)flag = true
            if(this.add_tags==='')this.$Message.error("请输入标注！");
            else if(this.loc<0)this.$Message.error("请选择图片！");
            else if(!this.ifDraw&&this.tagModel!=="全局标注")this.$Message.error("请绘制区域！");
            else{
              if(this.tagModel==="全局标注"){
                if(flag){
                  // this.tag_entire_list[this.loc].indexOf(this.add_tags)!==-1
                  this.$Message.error("存在相同标注！")
                }
                else if(this.tag_entire_list[this.loc].length===2){
                  this.$Message.error("全局标注数量达到上限!")
                }
                else{
                  this.tag_entire_list[this.loc][this.tag_entire_list[this.loc].length] = {'tag':''+this.add_tags,'loc':this.count};
                  this.count++
                  this.clearArea()
                }
              }
              else if(this.tagModel==='方框标注'){
                if(this.tag_box_list[this.loc].length===2){
                  this.$Message.error("方框标注数量达到上限!")
                }
                else{
                  this.tag_box_list[this.loc][this.tag_box_list[this.loc].length] = {'tag':''+this.add_tags,'loc':this.count};
                  this.point_box_list[this.loc][this.tag_box_list[this.loc].length-1] = {'x':this.x,'y':this.y,'targetx':this.targetx,'targety':this.targety}
                  this.count++
                  // console.log(this.tag_box_list);console.log(this.point_box_list)
                  this.clearArea()
                }

              }
              else{
                if(this.tag_area_list[this.loc].length===2){
                  this.$Message.error("区域标注数量达到上限!")
                }
                else{
                  this.tag_area_list[this.loc][this.tag_area_list[this.loc].length] = {'tag':''+this.add_tags,'loc':this.count};
                  this.point_area_list[this.loc][this.tag_area_list[this.loc].length-1] = {'x_list':this.x_list,'y_list':this.y_list}
                  // console.log(this.tag_area_list)
                  // console.log(this.point_area_list)
                  this.count++
                  this.clearArea()
                }

              }
            }
          }

        },
        handleClose2 (event, name) {
          console.log(name)
          let index = this.tag_entire_list[this.loc];
          for(let i in  this.tag_entire_list[this.loc]) if(name ===  this.tag_entire_list[this.loc][i].tag)index = i;
          this.tag_entire_list[this.loc].splice(index, 1);
          this.show_tag=false;this.show_tag=true
        },
        handleClose3 (event, name) {
          let index;
          for(let i=0;i<this.tag_box_list[this.loc].length;i++){
            if(this.tag_box_list[this.loc][i].loc === name) {
              index = i;
            }
          }
          this.tag_box_list[this.loc].splice(index, 1);
          this.point_box_list[this.loc].splice(index, 1);
          // console.log(this.point_box_list)
          // console.log(this.tag_box_list)
          this.show_tag=false;this.show_tag=true
        },
        handleClose4 (event, name) {
          let index;
          for(let i=0;i<this.tag_area_list[this.loc].length;i++){
            if(this.tag_area_list[this.loc][i].loc === name) {
              index = i;
            }
          }
          this.tag_area_list[this.loc].splice(index, 1);
          this.point_area_list[this.loc].splice(index, 1);
          this.show_tag=false;this.show_tag=true
        },
        ok: function () {
          this.clearArea();
          this.select_pic = 0;
          let yImg = new Image();
          let cxt = this.cxt_back;
          yImg.onload = function () {
            draw(this, cxt);
          };
          yImg.src = this.imgPath;
          function draw(obj, cxt) {
            cxt.drawImage(obj, 0, 0, 400,300);
          }
        },
        handleView (path,index) {
          this.imgPath = path;
          this.loc = index;
          // this.visible = true;
          this.ok()
        },
        changeModel(type){
          if(type===1){
            this.tagModel = "全局标注"
            this.$Message.success("已切换为全局标注！")
          }
          else if(type ===2){
            this.tagModel = "方框标注"
            this.$Message.success("已切换为方框标注！")
          }
          else{
            this.tagModel = "区域标注"
            this.$Message.success("已切换为区域标注！")
          }
        }
      }
    }
</script>

<style scoped>
  .choose_pic{
    height:80px;
  }
  .demo-upload-list{
    display: inline-block;
    width: 60px;
    height: 60px;
    text-align: center;
    line-height: 60px;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0,0,0,.2);
    margin-right: 4px;
  }
  .demo-upload-list img{
    width: 100%;
    height: 100%;
  }
  .demo-upload-list-cover{
    display: none;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0,0,0,.6);
  }
  .demo-upload-list:hover .demo-upload-list-cover{
    display: block;
  }
  .demo-upload-list-cover i{
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
  }
</style>
