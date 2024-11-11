<!-- 推荐歌曲页面 -->
<template>
  <div class="front-body">
    <el-card class="api-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          最新歌曲
        </div>
        <div v-if="lastList.length>0" class="body">
          <sui-info v-for="item in lastList" :key="item.id"
                    @click.native="play(item)"
                    :title="item.name"
                    :imgsrc="item.picture"
                    :content="item.introduction"
                    :date="item.createDate">
          </sui-info>
        </div>
        <el-empty v-else class="sui-empty" description="暂无数据"></el-empty>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          猜你喜欢
        </div>
        <el-carousel :interval="4000" type="card" height="300px">
          <el-carousel-item v-for="item in recommendList" :key="item.id">
            <sui-card @click.native="play(item)"
                      style="width: 300px;margin-left: 120px"
                      :title="item.name"
                      :imgsrc="item.picture"
                      :content="item.introduction"
                      :date="item.createDate">
            </sui-card>
          </el-carousel-item>
        </el-carousel>
      </el-card>
    </el-card>
  </div>
</template>>
<script>
    import {mapActions} from "vuex";
    import SuiCard from "../../../../components/sui-card/sui-card";

    export default {
      components: {SuiCard},
      data() {
        return {
          loading: true,//是否加载完成
          selectedData: [],//勾选数据
          total: 0, //记录数
          lastList: [],
          recommendList: [],
          queryForm: {
            current: 1,
            size: 10,
          }
        };
      },
      mounted() {
        //初始化数据
        this.getLastList();
        this.getRecommendList();
      },
      computed: {
        hasLogin() {
          return this.$store.getters.hasLogin;
        }
      },
      methods: {
        ...mapActions(['commitPlayList']),
        //播放音乐
        play(item){
          if(!this.hasLogin){
            this.showWarn("请先进行登录");
            return;
          }
          const that = this;
          that.$request.doPost('/api/playList/sub', {songId: item.id}).then(res => {
            if (res.isOk) {
              that.commitPlayList();
              that.toPage({name:'songDetails',query:{id:item.id}})
            };
          });
        },
        //获取最新数据
        getLastList() {
          this.$request.doGet('/api/recommend/getLastList', this.queryForm).then(res => {
            if (res.isOk) {
              this.loading = false;
              this.lastList = res.list;
              this.total = res.list.length;
            }
          });
        },
        //获取推荐数据
        getRecommendList() {
          this.$request.doGet('/api/recommend/getRecommendList', this.queryForm).then(res => {
            if (res.isOk) {
              this.loading = false;
              this.recommendList = res.list;
              this.total = res.list.length;
            }
          });
        },
      }
    };
</script>
