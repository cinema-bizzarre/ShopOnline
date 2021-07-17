angular.module('app', ['ngStorage']).controller('productController', function ($scope, $http, $location, $localStorage) {
    const contextPath = 'http://localhost:8189/shopOnline';

    $scope.itemsSum = 0;
    $scope.loadPage = function (page) {
        $http({
            url: contextPath + "/api/v1/products",
            method: "GET",
            params: {
                p: page
            }
        }).then(function (response) {
            $scope.productsPage = response.data;


            let minPageIndex = page - 2;
            if (minPageIndex < 1) {
                minPageIndex = 1;
            }

            let maxPageIndex = page + 2;
            if (maxPageIndex > $scope.productsPage.totalPages) {
                maxPageIndex = $scope.productsPage.totalPages;
            }

            $scope.paginationArray = $scope.generatePagesIndexes(minPageIndex, maxPageIndex);
            console.log($scope.productsPage);
            $scope.loadCart();

        });
    };


    $scope.createNewProduct = function () {
        console.log($scope.newProduct);
        $http.post(contextPath + '/api/v1/products', $scope.newProduct)
            .then(function successCallBack(response) {
                $scope.newProduct = null;
                $scope.loadPage($scope.productsPage.totalPages);
            }, function errorCallback(response) {
                console.log(response.data);
                alert("Error!!! \n" + response.data.messages);
            });
    };
    $scope.clickOnProduct = function (product) {
        console.log(product);
    }

    $scope.generatePagesIndexes = function (startPage, endPage) {
        let arr = [];
        for (let i = startPage; i < endPage + 1; i++) {
            arr.push(i);
        }
        return arr;
    }

     $scope.loadCart = function () {
        $http.get(contextPath + '/api/v1/cart')
            .then(function (response) {
                $scope.cartDto = response.data;
            });
    }

    $scope.addToCart = function (productId) {
        $http({
            url: contextPath + "/api/v1/cart/add/" + productId,
            method: "GET"
        }).then(function (response) {
            $scope.loadCart();
        })
    }

    $scope.incDec = function (title, value) {
        $http({
            url: contextPath + "/api/v1/cart/quantity",
            method: "GET",
            params: {
                title: title,
                incDec: value
            }
        }).then(function (response) {
            $scope.loadCart();
        })
    }

    $scope.clearCart = function () {
        $http({
            url: contextPath + "/api/v1/cart/clear",
            method: "GET"
        }).then(function (response) {
            $scope.loadCart();
        })
    }

    $scope.createNewOrder = function (address, phone) {
        $http({
            url: contextPath + "/api/v1/orders",
            method: "POST"
             params: {
                address: address,
                phone: phone
             }
        }).then(function successCallBack(response) {
            $scope.newOrder = null;
            $scope.clearCart();
            alert("Ваш заказ сформирован");
            $scope.showMyOrders();
        }, function errorCallback(response) {
            console.log(response.data);
            alert("Error!!! \n" + response.data.messages);
        });
    };

    $scope.showMyOrders = function (address, phone) {
        $http({
            url: contextPath + "/api/v1/orders",
            method: "GET"
        }).then(function successCallBack(response) {
            $scope.myOrdersSum=0;
            $scope.myOrders = response.data;
        }, function errorCallback(response) {
            console.log(response.data);
            alert("Error!!! \n" + response.data.messages);
        });
    };


    $scope.deleteItem = function (productTitle) {
        $http({
            url: contextPath + "/api/v1/cart/delete",
            method: "GET",
            params: {
                title: productTitle
            }
        }).then(function (response) {
            $scope.loadCart();
            console.log("OK");
        })
    }

    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.aprilMarketCurrentUser = {username: $scope.user.username, token: response.data.token};

                    $scope.user.username = null;
                    $scope.user.password = null;
                    $scope.showMyOrders();
                }
            }, function errorCallback(response) {
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
    };

    $scope.clearUser = function () {
        delete $localStorage.aprilMarketCurrentUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        if ($localStorage.aprilMarketCurrentUser) {
            return true;
        } else {
            return false;
        }
    };

    $scope.whoAmI = function () {
        $http({
            url: contextPath + '/api/v1/users/me',
            method: 'GET'
        }).then(function (response) {
            alert(response.data.username + ' ' + response.data.email);
        });
    };

    if ($localStorage.aprilMarketCurrentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.shopOnlineCurrentUser.token;
        $scope.showMyOrders();
    }


    $scope.loadPage(1);
    $scope.loadCart();
});
