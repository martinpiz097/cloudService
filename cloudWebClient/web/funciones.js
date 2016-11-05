
function mostrarID(id){
    alert(id);
}

function download(idCard){

  $.ajax({
      type: 'POST', // metodo de envio
      url: 'http://localhost:8084/pruebaSesiones/buscarUsuario.do',
                 // data tiene todos los parametros que le enviare por post
      data: {
          filtro: texto,
          tipoBusqueda: tipoBusqueda
      }
          // cuando la peticion se haya hecho
                 // en el html se enviara la respuesta
      }).done(function(respuesta){
          $("#resultado").html(respuesta);
      });
}

function rename(){

}
