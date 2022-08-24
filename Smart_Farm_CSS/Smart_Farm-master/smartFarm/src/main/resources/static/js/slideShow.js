
var imgs = 3;
var now = 0;

var slideIndex = 1;
showSlides(slideIndex);

// 다음, 이전 제어
function plusSlides(n) {
  showSlides(slideIndex += n);
}

// 사진 제어
function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  var i;
  var slides = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("dot");
  if (n > slides.length) {slideIndex = 1} 
  if (n < 1) {slideIndex = slides.length}
  for (i = 0; i < slides.length; i++) {
      slides[i].style.display = "none"; 
  }
  for (i = 0; i < dots.length; i++) {
      dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex-1].style.display = "block"; 
  dots[slideIndex-1].className += " active";
}

function slide() {
    now = now == imgs ? 0 : now += 1; 

    $(".imgs>img").eq(now - 1).css({"margin-left": "-500px"});
    $(".imgs>img").eq(now).css({"margin-left": "0px"});
}

function start() {
    $(".imgs>img").eq(0).siblings().css({"margin-left":"-500px"});
    
    setInterval(function () { slide()}, 2000);
}
start();




