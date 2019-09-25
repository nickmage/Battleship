function logout(){
    localStorage.removeItem('token');
    if (localStorage.getItem('id') !== null){
        localStorage.removeItem('id');
    }
    location.href='../';
}
