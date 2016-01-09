# Настройка Seaside #

<ol>
<blockquote><li> В качестве файлового веб-сервера теперь будет использоваться Nginx, поэтому можно перестать использовать Comanche и оставить минимум для работы с Seaside:</blockquote>

<pre><code>(HttpService services select:[:service| service portNumber=8080]) do:[:service| service stop].<br>
WAKomEncoded startOn: 8080<br>
</code></pre>
<blockquote></li></blockquote>

<blockquote><li> Файлы библиотек, используемых в приложении, должны находиться по адресу <b>/full-path-to-pharo-image/seaside/files/</b>. Чтобы выгрузить файлы библиотек из образа, необходимо выполнить:</blockquote>

<pre><code>mkdir -p /full-path-to-pharo-image/seaside/files/<br>
</code></pre>

<pre><code>FileDirectory setDefaultDirectory: '/full-path-to-pharo-image/seaside/files/'.<br>
<br>
(WAFileLibrary allSubclasses select: [:each | <br>
    each name beginsWithAnyOf: #('SP' 'SU' 'WA'  'PR' 'JQ')])<br>
    do: [:each | each deployFiles].<br>
<br>
FileDirectory setDefaultDirectory: '/full-path-to-pharo-image/'.<br>
</code></pre>

<blockquote></li></blockquote>

<blockquote><li> Чтобы исключить название Seaside-приложения из URI, необходимо выполнить:</blockquote>

<pre><code> (WADispatcher default entryPointAt: 'SmallPOS') preferenceAt: #serverPath put: '/'<br>
</code></pre>
<blockquote></li>
</ol></blockquote>

# Настройка Nginx #

```
sudo apt-get install nginx
sudo sh -c "echo > /etc/nginx/sites-available/smallpos.conf"
sudo ln -s /etc/nginx/sites-available/smallpos.conf /etc/nginx/sites-enabled/smallpos
```

<ol>
<blockquote><li> Содержание <b>/etc/nginx/nginx.conf</b>:<br>
<pre><code>user www-data;<br>
worker_processes 2;<br>
timer_resolution 100ms;<br>
worker_priority -5;<br>
pid /var/run/nginx.pid;<br>
<br>
events {<br>
	worker_connections 2048;<br>
	use epoll;<br>
}<br>
<br>
http {<br>
<br>
	# Basic Settings<br>
<br>
	sendfile on;<br>
	tcp_nopush on;<br>
	tcp_nodelay on;<br>
	keepalive_timeout 65;<br>
	types_hash_max_size 2048;<br>
	server_tokens off;<br>
<br>
	include /etc/nginx/mime.types;<br>
	default_type application/octet-stream;<br>
<br>
	# Gzip Settings<br>
<br>
	gzip on;<br>
	gzip_min_length 1100;<br>
	gzip_buffers 64 8k;<br>
	gzip_comp_level 9;<br>
	gzip_http_version 1.1;<br>
	gzip_proxied any;<br>
	gzip_types text/plain application/xml application/x-javascript text/css text/javascript;<br>
<br>
	# Virtual Host Configs<br>
<br>
	include /etc/nginx/conf.d/*.conf;<br>
	include /etc/nginx/sites-enabled/*;<br>
}<br>
</code></pre>
</li></blockquote>

<blockquote><li> Содержание <b>/etc/nginx/sites-available/smallpos.conf</b>:<br>
<pre><code>server<br>
{<br>
<br>
	log_format  gzip  '$remote_addr - $remote_user [$time_local] '<br>
			  '"$request" $status $bytes_sent '<br>
                	  '"$http_referer" "$http_user_agent" "$gzip_ratio"';<br>
<br>
	#Полные пути к логам. Если файлов нет, то они будут созданы автоматически.<br>
	access_log  /full-path-to-access-log/access.log  gzip;<br>
	error_log /full-path-to-error-log/error.log;<br>
<br>
	#В качестве аргумента для server_name может выступать как имя домена, так и ip-адрес.<br>
	server_name www.yourdomain.ru yourdomain.ru;<br>
	listen 80;<br>
<br>
	#Полный путь к образу Pharo<br>
	root /full-path-to-pharo-image/;<br>
<br>
	rewrite ^/$ /seaside/SmallPOS;<br>
	proxy_redirect /seaside/SmallPOS /;<br>
<br>
	location /seaside/SmallPOS<br>
	{<br>
		proxy_pass http://127.0.0.1:8080;<br>
		proxy_set_header Host $host;<br>
	}<br>
}<br>
</code></pre>
</li>
</ol>