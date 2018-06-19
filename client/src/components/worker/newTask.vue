<template>
  <Card style="width:550px">
    <i-input v-model="value" placeholder="输入关键词搜索..." style="width:250px" icon="search"/><br>
    <Page :current="1" :total="50" simple></Page>
  </Card>
  <!--<input v-model="value" placeholder="输入关键词搜索..." style="width:300px"/>-->

</template>
<script>
  export default {
    // util:{Input},
    data(){

      return{
        value:''
      }
    }
  }
</script>

<style scoped>
  .Card{
    width:350px;
  }
</style>
