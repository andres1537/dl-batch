cp -p /usr/local/deliveries/dl-batch-delivery.tar /usr/local/batch/dl-batch
chmod 777 /usr/local/batch/dl-batch/dl-batch-delivery.tar
cd /usr/local/batch/dl-batch
tar xvf dl-batch-delivery.tar
rm dl-batch-delivery.tar

// converting fron win to unix format the file, specifically the new lines character.
dos2unix batch_leegilesdata.sh

// descomprimir gz
gzip -d /usr/local/data/batchDBLP/dblp-2015-09-01.xml.gz