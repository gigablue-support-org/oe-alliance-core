SUMMARY = "Skin for Enigma2 (HD, FHD, UHD, 4K)"
MAINTAINER = "Team Kraven"
inherit allarch

require conf/license/license-gplv2.inc
require conf/python/python3-compileall.inc

inherit gitpkgv

SRCREV = "${AUTOREV}"
PV = "3.6.83+git${SRCPV}"
PKGV = "3.6.83+git${GITPKGV}"
VER="3.6.83"

RDEPENDS:${PN} = "${PYTHON_PN}-requests ${@bb.utils.contains("PYTHON_PN", "python", "${PYTHON_PN}-subprocess", "", d)} ${@bb.utils.contains("PYTHON_PN", "python", "${PYTHON_PN}-imaging", "${PYTHON_PN}-pillow", d)} enigma2-plugin-systemplugins-mphelp ${PYTHON_PN}-lxml"

SRC_URI="git://github.com/openatv/SevenHD.git;protocol=https"

FILES:${PN} = "/usr/*"

S = "${WORKDIR}/git/data"

do_install() {
    install -d ${D}/usr/share/enigma2
    cp -rp ${S}/usr ${D}/
    chmod -R a+rX ${D}/usr/share/enigma2/
}

pkg_postinst:${PN} () {
#!/bin/sh
if [ -d /tmp/user ]; then
    cp -r /tmp/user/* /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/user
fi

if [ -f /tmp/skin-user.xml ]; then
    cp /tmp/skin-user.xml /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/data
fi

echo "                                                          "
echo "             ...Skin successful installed.                "
echo "                                                          "
exit 0
}

pkg_postrm:${PN} () {
#!/bin/sh
rm -rf /usr/share/enigma2/SevenHD
rm -rf /usr/lib/enigma2/python/Plugins/Extensions/SevenHD
rm -rf /usr/lib/enigma2/python/Components/Converter/SevenHD*
rm -rf /usr/lib/enigma2/python/Components/Renderer/SevenHD*
echo " "
echo " ...Skin successful removed. "
echo " "
exit 0
}

pkg_preinst:${PN} () {
#!/bin/sh
echo "Checking for previous installations..."
if [ -d /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/user ]; then
    mkdir -p /tmp/user 
    cp -r /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/user/* /tmp/user
fi

if [ -f /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/data/skin-user.xml ]; then
    cp /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/data/skin-user.xml /tmp
fi

if [ -f /usr/lib/enigma2/python/Plugins/Extensions/SevenHD/plugin.py ]; then
    rm -rf /usr/lib/enigma2/python/Plugins/Extensions/SevenHD
    rm -rf /usr/lib/enigma2/python/Components/Converter/SevenHD*
    rm -rf /usr/lib/enigma2/python/Components/Renderer/SevenHD*
    echo "                                                           "
    echo "           SevenHD configuration plugin                    "
    echo "              was found and removed!                       "
    echo "                                                           "
fi

echo "                                                           "
echo "         The Skin SevenHD is now being installed...        "
echo "                                                           "
exit 0
}

pkg_prerm:${PN} () {
#!/bin/sh
echo " "
echo " The Skin SevenHD is now being removed... "
echo " "
exit 0
}

do_package_qa[noexec] = "1"

