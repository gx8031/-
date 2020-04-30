'use strict'
document.body.scrollIntoView(false);
setTimeout(() => {
  console.log('out');
}, 5000);


let event = document.createEvent("MouseEvents");

event.initMouseEvent(
  "click",
  true,
  true,
  document.defaultView,
  0,
  0,
  0,
  0,
  0,
  false,
  false,
  false,
  false,
  0,
  null
);


let timer = setInterval(() => {
  document.body.scrollIntoView(false);
  let btn = document.querySelector("#_ajax_spanallpost");
  btn.dispatchEvent(event);
  if (btn === null) {
    timer = null;
  }
}, 3000);

/* 上述代码模拟事件 */

// let array = document.querySelectorAll('[target="_blank"]');
let array = document.querySelectorAll('.meta-title');
let arrayUrls = [];
for(const el of array){
  // console.log(el.href);
  arrayUrls.push(el);
}


let xhr = new XMLHttpRequest();
xhr.open('post', "http://localhost:8123/image_get_war/upload_url", false);
xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;");
xhr.send("urls=" + "array");


xhr.onreadystatechange = function() {
  console.log(xhr.responseText);
  if (xhr.readyState == 4 && xhr.status == 200) {
    console.log(xhr.responseText);
  }
}
xhr.send("urls=" + arrayUrlss);




