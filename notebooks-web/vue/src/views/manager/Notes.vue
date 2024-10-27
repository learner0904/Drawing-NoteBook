<template>
    <div class="notes-container">
      <el-row :gutter="20">
        <!-- Card with plus sign -->
        <el-col :span="8">
          <div class="add-card" @click="openExamplePage">
            <div class="plus-sign">+</div>
          </div>
        </el-col>
  
        <!-- Existing notes -->
        <el-col :span="8" v-for="note in notes.slice().reverse()" :key="note.id">
          <el-card class="note-card" shadow="hover" style="margin-bottom: 20px; position: relative;">
            <!-- Note Picture -->
            <img :src="note.picture" alt="" style="max-height: 250px; width: 100%; object-fit: cover; display: block; margin: 0 auto;">
            
            <!-- Note Content -->
            <div class="note-content" style="background-color: rgba(0, 0, 0, 0.7); color: white; width: 100%; padding: 10px; position: absolute; bottom: 0; left: 0; display: flex; flex-direction: column; justify-content: space-between; align-items: flex-start;">
              <div style="display: flex; justify-content: space-between; width: 100%; align-items: center;">
                <p style="margin: 0;">{{ note.comment }}</p>
                <el-button type="primary" @click="goToD3Runner(note.id)" size="mini">查看详情</el-button>
              </div>
              <small style="color: #ccc; font-size: 12px; margin-top: 5px;">{{ note.date }}</small>
            </div>
          </el-card>
        </el-col>





      </el-row>

      <!-- Example modal -->
    <el-dialog
      title="示例选择"
      :visible.sync="exampleDialogVisible"
      width="60%"
    >
      <el-row :gutter="20">
        <!-- Add new D3 chart -->
        <el-col :span="8">
          <div class="add-new-card" @click="goToD3RunnerWithoutId">
            <div class="plus-sign">+</div>
          </div>
        </el-col>

        <!-- Example cards -->
        <el-col :span="8" v-for="example in examples" :key="example.id">
          <el-card class="example-card"  shadow="hover" style="margin-bottom: 20px; position: relative;">
            <img :src="example.picture" alt="Example Image" style="max-height: 100px; width: 100%; object-fit: cover;">
            <div class="example-content" style="text-align: center; margin-top: 10px;">
              <p>{{ example.name }}</p>
            </div>
            <!-- 添加按钮 -->
            <div style="text-align: center; margin-top: 10px;">
              <el-button type="primary" @click="showExample(example)">Show Example</el-button>
            </div>
          </el-card>
        </el-col>

      </el-row>
    </el-dialog>

    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        notes: [],
        exampleDialogVisible: false,
      examples: [
        {
          id: 1,
          name: "柱状图",
          picture: "http://localhost:9090/files/1728994611990-d3-output.png",
          code: `{
                  marks: [
                    Plot.barY(
                      [
                        { name: 'A', value: 30 },
                        { name: 'B', value: 80 },
                        { name: 'C', value: 45 },
                        { name: 'D', value: 60 },
                        { name: 'E', value: 20 },
                        { name: 'F', value: 90 },
                        { name: 'G', value: 55 }
                      ],
                      { x: "name", y: "value" }
                    )
                  ],
                  y: { label: "Value" },
                  x: { label: "Category" },
                  height: 300,
                  width: 500
                }
                `
        },
        {
          id: 2,
          name: "折线图",
          picture: "http://localhost:9090/files/1729008552098-d3-output.png",
          code: `data = "earthquake2.csv"
                grid: true,
                marks: [
                Plot.line(data, Plot.binX({y:"proportion"},{x: "Magnitude"})),
                Plot.ruleY([0]),
                Plot.ruleX([5.5])
                ]
                `
        },
        {
          id: 3,
          name: "散点图",
          picture: "http://localhost:9090/files/1728922455594-d3-output.png",
          code: `// 创建SVG画布
const svg = d3.select(container)
  .append('svg')
  .attr('width', 500)
  .attr('height', 500)
  .style('background-color', 'white');  // 设置背景为白色

// 数据集
const data = [
  {name: "孙", yuwen: 90, shuxue: 89, yingyu: 88},
  {name: "赵", yuwen: 78, shuxue: 88, yingyu: 66},
  {name: "周", yuwen: 93, shuxue: 79, yingyu: 83},
  {name: "李", yuwen: 96, shuxue: 77, yingyu: 89},
  {name: "王", yuwen: 87, shuxue: 75, yingyu: 72},
  {name: "章", yuwen: 92, shuxue: 83, yingyu: 90},
  {name: "刘", yuwen: 92, shuxue: 85, yingyu: 98},
  {name: "陈", yuwen: 79, shuxue: 91, yingyu: 86},
  {name: "杨", yuwen: 84, shuxue: 89, yingyu: 90},
  {name: "黄", yuwen: 95, shuxue: 90, yingyu: 92},
  {name: "吴", yuwen: 86, shuxue: 89, yingyu: 80},
  {name: "徐", yuwen: 77, shuxue: 93, yingyu: 90},
  {name: "马", yuwen: 85, shuxue: 87, yingyu: 80},
  {name: "胡", yuwen: 89, shuxue: 93, yingyu: 91},
  {name: "朱", yuwen: 75, shuxue: 89, yingyu: 91},
  {name: "郭", yuwen: 91, shuxue: 93, yingyu: 87},
  {name: "何", yuwen: 87, shuxue: 85, yingyu: 76},
  {name: "罗", yuwen: 82, shuxue: 85, yingyu: 90},
  {name: "高", yuwen: 88, shuxue: 91, yingyu: 74},
  {name: "林", yuwen: 80, shuxue: 85, yingyu: 90},
  {name: "姚", yuwen: 69, shuxue: 90, yingyu: 84},
  {name: "姜", yuwen: 74, shuxue: 88, yingyu: 76},
  {name: "夏", yuwen: 83, shuxue: 80, yingyu: 85},
  {name: "任", yuwen: 71, shuxue: 91, yingyu: 89},
];

// 定义x和y的比例尺
const xScale = d3.scaleLinear()
  .domain([60, 100])  // 假设成绩范围为60到100
  .range([50, 450]);  // 对应SVG中的坐标范围

const yScale = d3.scaleLinear()
  .domain([60, 100])
  .range([450, 50]);

// 添加虚线网格线
const xAxisGrid = d3.axisBottom(xScale).tickSize(-400).tickFormat('');
const yAxisGrid = d3.axisLeft(yScale).tickSize(-400).tickFormat('');

svg.append('g')
  .attr('class', 'x grid')
  .attr('transform', 'translate(0, 450)')
  .call(xAxisGrid)
  .selectAll('line')
  .style('stroke', 'lightgray')
  .style('stroke-dasharray', '4,4');  // 将网格线设置为虚线

svg.append('g')
  .attr('class', 'y grid')
  .attr('transform', 'translate(50, 0)')
  .call(yAxisGrid)
  .selectAll('line')
  .style('stroke', 'lightgray')
  .style('stroke-dasharray', '4,4');  // 将网格线设置为虚线

// 在图中绘制散点
svg.selectAll('circle')
  .data(data)
  .enter()
  .append('circle')
  .attr('cx', d => xScale(d.shuxue))  // 将数学成绩映射为x坐标
  .attr('cy', d => yScale(d.yuwen))   // 将语文成绩映射为y坐标
  .attr('r', 5)  // 点的半径
  .attr('fill', '#555');  // 将点的颜色设置为黑色偏灰

// 添加轴
const xAxis = d3.axisBottom(xScale);
const yAxis = d3.axisLeft(yScale);

svg.append('g')
  .attr('transform', 'translate(0, 450)')
  .call(xAxis);

svg.append('g')
  .attr('transform', 'translate(50, 0)')
  .call(yAxis);
`
        },
      ]
      };
    },
    created() {
      this.fetchNotes();
    },
    methods: {
        fetchNotes() {
            this.$request.get('/notes/selectAll')
              .then(res => {
                this.notes = res.data || []; // 假设后端返回的数据结构是正确的
                console.log("Fetched notes:", this.notes);
                console.log("Fetched notes:", this.res);
              })
              .catch(error => {
                console.error("Error fetching notes:", error);
              });
          },
          openExamplePage() {
      this.exampleDialogVisible = true;
    },
    showExample(example) {
      this.$router.push({ path: '/d3runner', query: { id: null, code: example.code, picture: example.picture } });
    },
        goToD3Runner(noteId) {
          this.$router.push({ path: '/d3runner', query: { id: noteId } });
        },
        goToD3RunnerWithoutId() {
          this.$router.push({ path: '/d3runner' });
        },
        getPictureUrl(picture) {
          // 如果 picture 已经是完整 URL，则直接返回
          if (picture.startsWith('http')) {
            return picture;
          }
          // 否则使用 baseUrl 拼接图片路径
          return this.$baseUrl + picture;
        }
      }
    };
  </script>
  
  <style scoped>
  .notes-container {
    padding: 20px;
  }
  
  .note-picture {
    width: 100%;
    height: 250px;
    object-fit: cover;
    border-radius: 15px;
  }
  
  .note-content {
    margin-top: 10px;
    text-align: center;
  }
  
  .note-card {
    border-radius: 15px;
    width: 100%;
    height: 350px; /* 调整卡片高度 */
  }
  
  .add-card {
    border: 2px dashed #d9d9d9;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 350px;
    width: 100%; /* 同 note-card 保持一致的大小 */
    border-radius: 15px;
    cursor: pointer;
    transition: border-color 0.3s ease;
  }
  
  .add-card:hover {
    border-color: #080f17;
  }
  
  .add-new-card {
    border: 2px dashed #d9d9d9;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 220px;
    width: 100%; /* 同 note-card 保持一致的大小 */
    border-radius: 15px;
    cursor: pointer;
    transition: border-color 0.3s ease;
  }
  
  .add-new-card:hover {
    border-color: #080f17;
  }
  .plus-sign {
    font-size: 60px;
    color: #9db5cd;
  }
  </style>
  