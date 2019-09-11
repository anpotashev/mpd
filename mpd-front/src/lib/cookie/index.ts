export function getArrayCookie(name: string) {
  let json_str = getCookie(name);
  if (json_str === undefined) return [];
  return JSON.parse(json_str);
}

export function setArrayCookie(name: string, array: any, options: any) {
  let json_str = JSON.stringify(array);
  setCookie(name, json_str, options);
}

export function getCookie(name: string) {
  let matches = document.cookie.match(new RegExp(
      "(?:^|; )" + name.replace(/([.$?*|{}()[\]\\/+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

export function getBooleanCookie(name: string, defaultValue = true) {
  let result = getCookie(name);
  return result
      ? result === 'true'
      : defaultValue;
}

export function setCookie(name: string, value: any, options: any) {
  options = options || {};

  let expires = options.expires;

  if (typeof expires == "number" && expires) {
    let d = new Date();
    d.setTime(d.getTime() + expires * 1000);
    expires = options.expires = d;
  }
  if (expires && expires.toUTCString) {
    options.expires = expires.toUTCString();
  }

  value = encodeURIComponent(value);

  let updatedCookie = name + "=" + value;

  for (let propName in options) {
    updatedCookie += "; " + propName;
    let propValue = options[propName];
    if (propValue !== true) {
      updatedCookie += "=" + propValue;
    }
  }

  document.cookie = updatedCookie;
}

export function deleteCookie(name: string) {
  setCookie(name, "", {
    expires: -1
  })
}