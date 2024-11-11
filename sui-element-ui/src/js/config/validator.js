import axios from "axios";
import qs from 'qs'

//唯一校验
function validatorUnique(rule, value, callback) {
  if (value!=undefined && value.trim() !== '') {
    let form = rule.form;
    let tableName = rule.tableName;
    let id = form[rule.model].id == undefined ? "" : form[rule.model].id;
    let params = {
      fields: {},
      tableName: tableName,
      id: id
    };
    params.fields[rule.column] = value.trim();
    let data = qs.stringify(params);
    axios.post("/api/sys/unique", data).then(function (res) {
      if (res.data.isOk) {
        callback()
      } else {
        callback(new Error('唯一项不能重复'))
      }
    })
  } else {
    callback()
  }
}
//手机号
function validatorPhone(rule, value, callback) {
  const reg = /^(?:(?:\+|00)86)?1[3-9]\d{9}$/
  if (value!=undefined && value.trim() !== '' && !reg.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}
//价格
function validatorPrice(rule, value, callback) {
  const reg = /^[0-9]+\.[0-9]{2}$/
  if (value!=undefined && !reg.test(value)) {
    callback(new Error('请保输入数字并留两位小数'))
  } else {
    callback()
  }
}
//正整数:大于等于0
function validatorInt(rule, value, callback) {
  const reg = /^[+]{0,1}(\d+)$/;
  if (value!=undefined && !reg.test(value)) {
    callback(new Error('请保输入大于或等于0的整数'))
  } else {
    callback()
  }
}
//身份证
function validatorIdCard(rule, value, callback) {
  const reg = /(^\d{8}(0\d|10|11|12)([0-2]\d|30|31)\d{3}$)|(^\d{6}(18|19|20)\d{2}(0\d|10|11|12)([0-2]\d|30|31)\d{3}(\d|X|x)$)/
  if (value!=undefined && value.trim() !== '' && !reg.test(value)) {
    callback(new Error('请输入正确的身份证号'))
  } else {
    callback()
  }
}
//中文名字
function validatorChineseName(rule, value, callback) {
  const reg = /^(?:[\u4e00-\u9fa5·]{2,16})$/
  if (value!=undefined && value.trim() !== '' && !reg.test(value)) {
    callback(new Error('请输入正确的中文名字'))
  } else {
    callback()
  }
}

export default{
  unique: validatorUnique,
  phone: validatorPhone,
  price: validatorPrice,
  int: validatorInt,
  idCard: validatorIdCard,
  chineseName: validatorChineseName,
}
