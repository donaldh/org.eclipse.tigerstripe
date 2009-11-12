#!/bin/bash

if [ $# -ne 3 ]; then
    echo "usage: patch-versions.sh source-codebase destination version-string"
    exit 1
fi

XML=/usr/cisco/bin/xml

# set -x

SRC=$1; shift
DST=$1; shift
VERSION_STRING=$1.qualifier; shift


DST=$(mkdir -p $DST; cd $DST; pwd)

( cd $SRC
  find . -maxdepth 6 -type f -name MANIFEST.MF -o -name feature.xml| while read ofn; do
      if ! echo $ofn | grep -q '/target/' >/dev/null ; then
          nfn=$DST/$ofn

          case $ofn in
          */MANIFEST.MF) 
              if egrep 'Bundle-Version:[[:blank:]]*0\.0\.0' >/dev/null $ofn; then
                  mkdir -p `dirname "$nfn"`
                  echo "[versioning $nfn]"
                  sed -e "s/Bundle-Version:\\s*0\.0\.0\\s*$/Bundle-Version: $VERSION_STRING/g" "$ofn" > "$nfn"
              fi
              ;;
          */feature.xml) 
              if [ x`$XML sel -t -v '/feature/@version' $ofn` == "x0.0.0" ]; then
                  mkdir -p `dirname "$nfn"`
                  echo "[versioning $nfn]"
                  $XML ed -u '/feature/@version' -v "$VERSION_STRING" "$ofn" >"$nfn"
              fi
              ;;
          esac
      fi
  done
)