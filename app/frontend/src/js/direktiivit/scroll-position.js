// Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
//
// This program is free software:  Licensed under the EUPL, Version 1.1 or - as
// soon as they will be approved by the European Commission - subsequent versions
// of the EUPL (the "Licence");
//
// You may not use this work except in compliance with the Licence.
// You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// European Union Public Licence for more details.

'use strict';

angular.module('osaan.direktiivit.scroll-position', [])
  .directive("scrollPosition", ['$window', function ($window) {
    return {
      restrict: 'A',
      scope: false,
      link: function(scope) {
        angular.element($window).bind("scroll", function() {
          
          var clientHeight = $window.document.body.clientHeight - $(window).height();
          var position =  ((this.pageYOffset/clientHeight)*100);
          scope.scrollPosition = 'top';

          if(position < 5){
            scope.scrollPosition = 'top';
          }else if(position > 5 && position < 95){
            scope.scrollPosition = 'middle';
          }else if(position > 95){
            scope.scrollPosition = 'bottom';
          }

          scope.$digest();
        });
      }
    };
  }]);
